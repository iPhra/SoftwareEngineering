package it.polimi.se2018.mvc.controller;

import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.PrivateObjective;
import it.polimi.se2018.mvc.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;
import it.polimi.se2018.network.Server;
import it.polimi.se2018.network.connections.ServerConnection;
import it.polimi.se2018.network.messages.requests.SetupMessage;
import it.polimi.se2018.network.messages.responses.DisconnectionResponse;
import it.polimi.se2018.network.messages.responses.TimeUpResponse;
import it.polimi.se2018.network.messages.responses.ReconnectionNotificationResponse;
import it.polimi.se2018.network.messages.responses.sync.ReconnectionResponse;
import it.polimi.se2018.network.messages.responses.sync.SetupResponse;
import it.polimi.se2018.utils.DeckBuilder;
import it.polimi.se2018.utils.Stopper;
import it.polimi.se2018.utils.WaitingThread;
import it.polimi.se2018.utils.WindowBuilder;
import it.polimi.se2018.mvc.view.ServerView;


import java.time.Duration;
import java.util.*;

public class GameManager implements Stopper {
    private final Server server;
    private final DeckBuilder deckBuilder;
    private ServerView serverView;
    private Controller controller;
    private Board model;
    private final List<Integer> playerIDs;
    private final Map<Integer,ServerConnection> serverConnections; //maps playerID to its connection
    private final Map<Integer, String> playerNames; //maps playerID to its name
    private final Map<Integer, List<Window>> windowsSetup;
    private final List<Player> players;
    private final List<Integer> disconnectedPlayers;
    private List<PrivateObjective> privateObjectives;
    private List<PublicObjective> publicObjectives;
    private List<ToolCard> toolCards;
    private List<Window> windows;
    private int setupsCompleted;
    private boolean matchCreated;
    private boolean matchStarted;
    private WaitingThread clock;

    public GameManager(Server server){
        this.server = server;
        windowsSetup = new HashMap<>();
        deckBuilder = new DeckBuilder();
        playerIDs = new ArrayList<>();
        serverConnections = new HashMap<>();
        playerNames = new HashMap<>();
        players = new ArrayList<>();
        disconnectedPlayers = new ArrayList<>();
        setupsCompleted = 0;
        matchCreated = false;
        matchStarted = false;
    }

    private void startTimer() {
        Duration timeout = Duration.ofSeconds(10);
        clock = new WaitingThread(timeout, this);
        clock.start();
    }

    //create a privateObjective for each player (does not assign them to the players)
    private void createPrivateObjectives() {
        privateObjectives = new ArrayList<>();
        privateObjectives = deckBuilder.extractPrivateObjectives(playerIDs.size());
    }

    //create the 3 public objectives of the game
    private void createPublicObjectives(){
        publicObjectives = new ArrayList<>();
        publicObjectives = deckBuilder.extractPublicObjectives(3);
    }

    //create the 3 toolcards of the game
    private void createToolCards(){
        toolCards = new ArrayList<>();
        toolCards = deckBuilder.extractToolCards(3);
    }

    private void createMVC() {
        matchStarted = true;
        Collections.shuffle(players);
        model = new Board(players, (toolCards.toArray(new ToolCard[0])), publicObjectives.toArray(new PublicObjective[0]));
        model.register(serverView);
        controller.setModel(model);
        controller.startMatch();
    }

    //returns the list of players who haven't chosen a map yet
    private List<Integer> getMissingPlayers() {
        List<Integer> missingPlayers = new ArrayList<>(playerIDs);
        for(Player player : players) {
            missingPlayers.remove(missingPlayers.indexOf(player.getId()));
        }
        return missingPlayers;
    }

    //create 4 "faces" of window for each player (does not assign them to the players,
    // note that players must choose their own window
    private void createWindows(){
        windows = new ArrayList<>();
        windows = WindowBuilder.extractWindows(playerIDs.size());
    }

    private int getLastPlayer() {
        for(Player player: players) if(!disconnectedPlayers.contains(player.getId())) return player.getId();
        return 0;
    }

    private void notifyReconnection(int playerID, boolean setup) {
        notifyOtherPlayers(playerID);
        ReconnectionResponse response = new ReconnectionResponse(playerID,!setup);
        if(!setup) {
            response.setModelView(new ModelView(model));
            response.setPublicObjectives(publicObjectives);
            response.setPrivateObjective(model.getPlayerByID(playerID).getPrivateObjective());
            response.setToolCards(toolCards);
        }
        response.setPlayersNumber(playerIDs.size());
        serverView.update(response);
    }

    private void notifyOtherPlayers(int playerID) {
        for(int id : playerIDs) {
            if (id != playerID) {
                serverView.update(new ReconnectionNotificationResponse(id, playerNames.get(playerID)));
            }
        }
    }

    private void reconnect(int playerID, ServerConnection serverConnection, boolean setup) {
        serverConnection.setServerView(serverView);
        serverConnections.put(playerID,serverConnection);
        disconnectedPlayers.remove(disconnectedPlayers.indexOf(playerID));
        serverView.addServerConnection(playerID,serverConnection);
        notifyReconnection(playerID, setup);
    }

    public ServerConnection getServerConnection(int playerID) {
        return serverConnections.get(playerID);
    }

    public void addServerConnection(int playerID ,ServerConnection serverConnection) {
        this.serverConnections.put(playerID, serverConnection);
    }

    public void addPlayerName(int playerID, String playerName) {
        playerNames.put(playerID,playerName);
    }

    public void addPlayerID(int playerID){
        playerIDs.add(playerID);
    }

    public void setServerView(ServerView serverView){
        this.serverView = serverView;
    }

    public ServerView getServerView() {
        return serverView;
    }

    public void removePlayer(int playerID) {
        serverConnections.remove(playerID);
        playerNames.remove(playerID);
        playerIDs.remove(playerIDs.indexOf(playerID));
        serverView.removePlayerConnection(playerID);
    }

    public boolean isMatchCreated() {
        return matchCreated;
    }

    public boolean isMatchStarted() {
        return matchStarted;
    }

    //called by server, it can be called even if there are not all the players still
    public int playersNumber(){
        return playerIDs.size();
    }

    public void startSetup(){
        controller = new Controller(this,serverView);
        serverView.register(controller);
        createPublicObjectives();
        createToolCards();
    }

    public void sendWindows(){
        matchCreated = true;
        createPrivateObjectives();
        createWindows();
        for(int i=0;i<playerIDs.size();i++) {
            windowsSetup.put(playerIDs.get(i),new ArrayList<>());
            for(int j=i*4;j<i*4+4;j++){
                windowsSetup.get(playerIDs.get(i)).add(windows.get(j));
            }
            serverView.update(new SetupResponse(
                    playerIDs.get(i),windowsSetup.get(playerIDs.get(i)),publicObjectives,privateObjectives.get(i),toolCards,playerIDs.size()));
        }
        startTimer();
    }

    public boolean isDisconnected(int playerID) {
        return disconnectedPlayers.contains(playerID);
    }

    public void setDisconnected(int playerID) {
        disconnectedPlayers.add(playerID);
        serverView.removePlayerConnection(playerID);
        serverConnections.get(playerID).stop();
        serverConnections.remove(playerID);
        if (disconnectedPlayers.size() == playerIDs.size() - 1) {
            controller.endMatch();
        }
        else {
            for (int id : playerIDs) serverView.handleNetworkOutput(new DisconnectionResponse(id, null, playerNames.get(playerID)));
        }
    }

    public void setReconnected(int playerID, ServerConnection serverConnection) {
        if(getMissingPlayers().contains(playerID)) {
            reconnect(playerID,serverConnection,true);
            createPlayer(new SetupMessage(playerID,0,windowsSetup.get(playerID).get(new Random().nextInt(4))));
        }
        else reconnect(playerID, serverConnection,false);
    }

    //when a player sends the map he chose
    public void createPlayer(SetupMessage setupMessage){
        windowsSetup.remove(setupMessage.getPlayerID());
        players.add(new Player(playerNames.get(setupMessage.getPlayerID()),setupMessage.getPlayerID(),setupMessage.getWindow(),privateObjectives.get(setupsCompleted)));
        setupsCompleted++;
        //when every played sent his window
        if(setupsCompleted==playerIDs.size()) {
            clock.interrupt();
            createMVC();
        }
    }

    public void endGame() {
        matchCreated = false;
        matchStarted = false;
        for (ServerConnection connection : serverConnections.values()) {
            connection.stop();
        }
        for(String nickname: playerNames.values()) {
            server.deregisterPlayer(nickname);
        }
        server.deregisterMatch(playerIDs.get(0)/1000);
    }

    @Override
    public void halt(String message) {
        Random random = new Random();
        List<Integer> missingPlayers = getMissingPlayers();
        for(int id : missingPlayers) {
            serverView.handleNetworkOutput(new TimeUpResponse(id));
            players.add(new Player(playerNames.get(id),id,windowsSetup.get(id).get(random.nextInt(4)),privateObjectives.get(setupsCompleted)));
            setupsCompleted++;
        }
        windowsSetup.clear();
        createMVC();
    }
}
