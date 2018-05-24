package it.polimi.se2018.mvc.controller;

import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.PrivateObjective;
import it.polimi.se2018.mvc.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;
import it.polimi.se2018.network.connections.ServerConnection;
import it.polimi.se2018.network.messages.requests.SetupMessage;
import it.polimi.se2018.network.messages.responses.SetupResponse;
import it.polimi.se2018.utils.DeckBuilder;
import it.polimi.se2018.utils.Timing;
import it.polimi.se2018.utils.WaitingThread;
import it.polimi.se2018.utils.WindowBuilder;
import it.polimi.se2018.mvc.view.ServerView;


import java.time.Duration;
import java.util.*;

public class GameManager implements Timing{
    private final DeckBuilder deckBuilder;
    private ServerView serverView;
    private Controller controller;
    private final List<Integer> playerIDs;
    private final Map<Integer,ServerConnection> serverConnections; //maps playerID to its connection
    private final Map<Integer, String> playerNames; //maps playerID to its name
    private final Map<Integer, List<Window>> windowsSetup;
    private final List<Player> players;
    private List<PrivateObjective> privateObjectives;
    private List<PublicObjective> publicObjectives;
    private List<ToolCard> toolCards;
    private List<Window> windows;
    private int setupsCompleted;
    private boolean matchPlaying;
    private WaitingThread clock;

    public GameManager(){
        windowsSetup = new HashMap<>();
        deckBuilder = new DeckBuilder();
        playerIDs = new ArrayList<>();
        serverConnections = new HashMap<>();
        playerNames = new HashMap<>();
        players = new ArrayList<>();
        setupsCompleted = 0;
        matchPlaying = false;
    }

    private void startTimer() {
        Duration timeout = Duration.ofSeconds(20);
        clock = new WaitingThread(timeout, this);
        clock.start();
    }

    private void createMVC() {
        Collections.shuffle(players);
        Board board = new Board(players, (toolCards.toArray(new ToolCard[0])), publicObjectives.toArray(new PublicObjective[0]));
        board.register(serverView);
        controller.setModel(board);
        matchPlaying = true;
        controller.startMatch();
    }

    //create 4 "faces" of window for each player (does not assign them to the players,
    // note that players must choose their own window
    private void createWindows(){
        windows = new ArrayList<>();
        windows = WindowBuilder.extractWindows(playerIDs.size());
    }

    public void addServerConnection(int playerID ,ServerConnection serverConnection) {
        this.serverConnections.put(playerID, serverConnection);
    }

    public void addPlayerName( int playerID, String playerName) {
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
        playerIDs.remove(playerID);
    }

    public boolean isMatchPlaying() {
        return matchPlaying;
    }

    //called by server
    public boolean checkName(String playerName){
        for(Integer player : playerNames.keySet()) {
            if (playerName.equals(playerNames.get(player))) return false;
        }
        return true;
    }

    //called by server, it can be called even if there are not all the players still
    public int playersNumber(){
        return playerIDs.size();
    }

    public void startSetup(){
        controller = new Controller(this);
        serverView.register(controller);
        createPublicObjectives();
        createToolCards();
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

    public void sendWindows(){
        createPrivateObjectives();
        createWindows();
        for(int i=0;i<playerIDs.size();i++) {
            windowsSetup.put(playerIDs.get(i),new ArrayList<>());
            for(int j=i*4;j<i*4+4;j++){
                windowsSetup.get(playerIDs.get(i)).add(windows.get(j));
            }
            serverView.update(new SetupResponse(
                    playerIDs.get(i),windowsSetup.get(playerIDs.get(i)),publicObjectives,privateObjectives.get(i),toolCards,new ArrayList<>(playerNames.values())));
        }
        startTimer();
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
        matchPlaying = false;
        for (ServerConnection connection : serverConnections.values()) {
            connection.stop();
        }
    }

    @Override
    public void wakeUp() {
        List<Integer> missingPlayers = playerIDs;
        Random random = new Random();
        for(Player player : players) {
            missingPlayers.remove(missingPlayers.indexOf(player.getId()));
        }
        for(int id : missingPlayers) {
            players.add(new Player(playerNames.get(id),id,windowsSetup.get(id).get(random.nextInt(4)),privateObjectives.get(setupsCompleted)));
            setupsCompleted++;
        }
        windowsSetup.clear();
        createMVC();
    }
}
