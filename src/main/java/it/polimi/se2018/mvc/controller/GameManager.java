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
import it.polimi.se2018.network.messages.responses.sync.modelupdates.ModelViewResponse;
import it.polimi.se2018.utils.DeckBuilder;
import it.polimi.se2018.utils.Stopper;
import it.polimi.se2018.utils.WaitingThread;
import it.polimi.se2018.utils.WindowBuilder;
import it.polimi.se2018.mvc.view.ServerView;


import java.io.*;
import java.time.Duration;
import java.util.*;

public class GameManager implements Stopper {
    private final Server server;
    private final DeckBuilder deckBuilder;
    private final List<Integer> playerIDs;
    private final Map<Integer,ServerConnection> serverConnections; //maps playerID to its connection
    private final Map<Integer, String> playerNames; //maps playerID to its name
    private final Map<Integer, List<Window>> windowsSetup;
    private final List<Player> players;
    private final List<Integer> disconnectedPlayers;
    private ServerView serverView;
    private Controller controller;
    private Board model;
    private List<PrivateObjective> privateObjectives;
    private List<PublicObjective> publicObjectives;
    private List<ToolCard> toolCards;
    private List<Window> windows;
    private int setupsCompleted;
    private boolean matchCreated;
    private boolean matchPlaying;
    private WaitingThread clock;
    private Duration timeout;

    public GameManager(Server server){
        this.server = server;
        getDuration();
        windowsSetup = new HashMap<>();
        deckBuilder = new DeckBuilder();
        playerIDs = new ArrayList<>();
        serverConnections = new HashMap<>();
        playerNames = new HashMap<>();
        players = new ArrayList<>();
        disconnectedPlayers = new ArrayList<>();
        setupsCompleted = 0;
        matchCreated = false;
        matchPlaying = false;
    }

    /**
     * Sets the duration of the timer for choosing a window
     */
    private void getDuration() {
        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("TimerProperties.txt");
        try(BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String[] tokens = sb.toString().split(";");
            timeout = Duration.ofSeconds(Integer.parseInt(tokens[1].split(":")[1]));
        }
        catch (IOException e) {
        timeout = Duration.ofSeconds(60);
        }
    }

    /**
     * Creates a new timer Thread and runs it
     */
    private void startTimer() {
        clock = new WaitingThread(timeout, this);
        clock.start();
    }

    /**
     * @return the id of the last player connected to the server
     */
    private int getLastPlayerID() {
        for(int id : playerIDs) {
            if (!disconnectedPlayers.contains(id)) return id;
        }
        return 0;
    }

    /**
     * @param playerID is the id of the player you want to know the nickname of
     * @return the nickname associated to a specific playerID
     */
    public String getNicknameById(int playerID) {
        return playerNames.get(playerID);
    }

    /**
     * @param playerID is the id of the player you want to get the ServerConnection of
     * @return the ServerConnection associated to a specific playerID
     */
    public ServerConnection getServerConnection(int playerID) {
        return serverConnections.get(playerID);
    }

    /**
     * @param playerID is the id of the player you want to add the ServerConnection of
     * @param serverConnection is the player's ServerConnection
     */
    public void addServerConnection(int playerID ,ServerConnection serverConnection) {
        this.serverConnections.put(playerID, serverConnection);
    }

    public void setMatchPlaying(boolean matchPlaying) {
        this.matchPlaying = matchPlaying;
    }

    /**
     * @param playerID is the id of the player you want to add to the lobby
     * @param playerName is the nickname of the player you want to add to the lobby
     */
    public void addPlayerName(int playerID, String playerName) {
        playerNames.put(playerID,playerName);
    }

    /**
     * @param playerID is the id of the player to add to the lobby
     */
    public void addPlayerID(int playerID){
        playerIDs.add(playerID);
    }

    /**
     * Sets the ServerView
     * @param serverView is the ServerView associated to this match
     */
    public void setServerView(ServerView serverView){
        this.serverView = serverView;
    }

    /**
     * @return the ServerView associated to this match
     */
    public ServerView getServerView() {
        return serverView;
    }

    /**
     * @return {@code true} if the match has been created, i.e. the timer in {@link Server} has ran out and two or more players are connected
     */
    public boolean isMatchCreated() {
        return matchCreated;
    }

    /**
     * @return {@code true} if all players chose their windows, or time has ran out
     */
    public boolean isMatchPlaying() {
        return matchPlaying;
    }

    /**
     * @param playerID is the id of the player you want to check if he's disconnected
     * @return {@code true} if the player is disconnected
     */
    public boolean isDisconnected(int playerID) {
        return disconnectedPlayers.contains(playerID);
    }

    /**
     * @return the number of players actually connected to the game, even just in the lobby
     */
    public int playersNumber(){
        return playerIDs.size();
    }

    /**
     * Creates 4 windows for each player in the match, does not assign them yet
     */
    private void createWindows(){
        windows = new ArrayList<>();
        windows = WindowBuilder.extractWindows(playerIDs.size());
    }

    /**
     * Creates a Private Objective for each player, does not assign it yet
     */
    private void createPrivateObjectives() {
        privateObjectives = new ArrayList<>();
        privateObjectives = deckBuilder.extractPrivateObjectives(playerIDs.size());
    }

    /**
     * Creates the three Public Objectives for this match
     */
    private void createPublicObjectives(){
        publicObjectives = new ArrayList<>();
        publicObjectives = deckBuilder.extractPublicObjectives(3);
    }

    /**
     * Creates the three Tool Cards for this match
     */
    private void createToolCards(){
        toolCards = new ArrayList<>();
        toolCards = deckBuilder.extractToolCards(3);
    }

    /**
     * Called after all the players chose their windows, or after the timer has ran out
     * Creates the model and starts the match
     */
    private void createMVC() {
        matchPlaying = true;
        Collections.shuffle(players);
        model = new Board(players, (toolCards.toArray(new ToolCard[0])), publicObjectives.toArray(new PublicObjective[0]));
        model.register(serverView);
        controller.setModel(model);
        controller.startMatch();
    }

    /**
     * @return the list of all players who haven't chosen a map yet
     * Used to notify the missing players that time has ran out
     */
    //returns the list of players who haven't chosen a map yet
    private List<Integer> getMissingPlayers() {
        List<Integer> missingPlayers = new ArrayList<>(playerIDs);
        for(Player player : players) {
            missingPlayers.remove(missingPlayers.indexOf(player.getId()));
        }
        return missingPlayers;
    }

    /**
     * Notifies all players in the game that a player has reconnected
     * @param playerID is the player who has just reconnected
     */
    private void notifyOtherPlayers(int playerID) {
        for(int id : playerIDs) {
            if (id != playerID) {
                serverView.update(new ReconnectionNotificationResponse(id, playerNames.get(playerID)));
            }
        }
    }

    /**
     * Used to send all the informations needed to a player who has reconnected, and notifies other players
     * @param playerID is the player who has just reconnected
     * @param isWindowSelection is {@code true} if the player has reconnected during window selection
     */
    private void notifyReconnection(int playerID, boolean isWindowSelection) {
        notifyOtherPlayers(playerID);
        ReconnectionResponse response;
        if(!isWindowSelection) {
            response = new ReconnectionResponse(playerID,true);
            response.setModelViewResponse(new ModelViewResponse(model,new ModelView(model),playerID));
        }
        else response = new ReconnectionResponse(playerID,false);
        response.setPlayersNumber(playerIDs.size());
        serverView.update(response);
    }

    /**
     * Used to set all attributes of the player who has reconnected, then notifies everyone
     * @param playerID is the player who has just reconnected
     * @param serverConnection is the new {@link ServerConnection} assigned to that player
     * @param isWindowSelection is {@code true} if the player has reconnected during window selection
     */
    private void reconnect(int playerID, ServerConnection serverConnection, boolean isWindowSelection) {
        serverConnection.setServerView(serverView);
        serverConnections.put(playerID,serverConnection);
        disconnectedPlayers.remove(disconnectedPlayers.indexOf(playerID));
        serverView.addServerConnection(playerID,serverConnection);
        notifyReconnection(playerID, isWindowSelection);
    }

    /**
     * Creates the controller and creates Public Objectives and Tool Cards
     */
    public void startSetup(){
        controller = new Controller(this,serverView);
        serverView.register(controller);
        createPublicObjectives();
        createToolCards();
    }

    /**
     * Sends 4 windows to each player in the game, then starts the timer
     */
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
                    playerIDs.get(i),windowsSetup.get(playerIDs.get(i)),privateObjectives.get(i),playerIDs.size()));
        }
        startTimer();
    }

    /**
     * Removes all the attributes of a player who has disconnected before the match was created
     * @param playerID is the player who has just disconnected
     */
    public void removePlayer(int playerID) {
        serverConnections.remove(playerID);
        playerNames.remove(playerID);
        playerIDs.remove(playerIDs.indexOf(playerID));
        serverView.removePlayerConnection(playerID);
    }

    /**
     * Removes all the attributes of a player who has just disconnected
     * If there is only one player left, after this disconnection, that player wins
     * @param playerID is the player who has just disconnected
     */
    public void setDisconnected(int playerID) {
        disconnectedPlayers.add(playerID);
        serverView.removePlayerConnection(playerID);
        serverConnections.get(playerID).stop();
        serverConnections.remove(playerID);
        if (disconnectedPlayers.size() == playerIDs.size() - 1) {
            clock.interrupt();
            controller.endMatchAsLast(!matchPlaying,getLastPlayerID());
        }
        else {
            for (int id : playerIDs) serverView.handleNetworkOutput(new DisconnectionResponse(id, playerNames.get(playerID)));
        }
    }

    public void setReconnected(int playerID, ServerConnection serverConnection) {
        if(getMissingPlayers().contains(playerID) && getMissingPlayers().size()==1) { //if it's window selection and i'm the last one who has to choose his window
            createPlayer(new SetupMessage(playerID,0,windowsSetup.get(playerID).get(new Random().nextInt(4))));
        }
        else if(getMissingPlayers().contains(playerID)) { //if it's window selection but i'm not the last one who has to choose his window
            reconnect(playerID,serverConnection,true);
        }
        else reconnect(playerID, serverConnection,false); //if it's not window selection
    }

    //when a player sends the map he chose
    public void createPlayer(SetupMessage setupMessage){
        int playerID = setupMessage.getPlayerID();
        windowsSetup.remove(playerID);
        players.add(new Player(playerNames.get(playerID),playerID,setupMessage.getWindow(),privateObjectives.get(playerIDs.indexOf(playerID))));
        setupsCompleted++;
        //when every played sent his window
        if(setupsCompleted==playerIDs.size()) {
            clock.interrupt();
            createMVC();
        }
    }

    public void endGame() {
        for (ServerConnection connection : serverConnections.values()) {
            connection.stop();
        }
        for(String nickname: playerNames.values()) {
            server.deregisterPlayer(nickname);
        }
        server.deregisterMatch(playerIDs.get(0)/1000);
        matchCreated = false;
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