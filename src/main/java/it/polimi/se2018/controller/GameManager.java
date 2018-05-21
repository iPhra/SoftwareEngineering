package it.polimi.se2018.controller;

import it.polimi.se2018.model.Board;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Window;
import it.polimi.se2018.model.objectives.privateobjectives.PrivateObjective;
import it.polimi.se2018.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.model.toolcards.ToolCard;
import it.polimi.se2018.network.connections.ServerConnection;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.requests.SetupMessage;
import it.polimi.se2018.network.messages.responses.SetupResponse;
import it.polimi.se2018.utils.DeckBuilder;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.utils.WindowBuilder;
import it.polimi.se2018.view.ServerView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManager implements Observer<Message>{
    private DeckBuilder deckBuilder;
    private ServerView serverView;
    private Board board;
    private Controller controller;
    private List<Integer> playerIDs;
    private Map<Integer,ServerConnection> serverConnections; //maps playerID to its connection
    private Map<Integer, String> playerNames; //maps playerID to its name
    private List<Player> players;
    private List<PrivateObjective> privateObjectives;
    private List<PublicObjective> publicObjectives;
    private List<ToolCard> toolCards;
    private List<Window> windows;
    private int setupsCompleted;

    public GameManager(DeckBuilder deckBuilder){
        this.deckBuilder = deckBuilder;
        this.playerIDs = new ArrayList<>();
        this.serverConnections = new HashMap<>();
        this.playerNames = new HashMap<>();
        this.players = new ArrayList<>();
        setupsCompleted = 0;
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

    private void createMVC() {
        board = new Board(players, (toolCards.toArray(new ToolCard[0])), publicObjectives.toArray(new PublicObjective[0]));
        board.register(serverView);
        controller.setModel(board);
        controller.startMatch();
    }

    public void startSetup(){
        controller = new Controller();
        controller.register(this);
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

    //create 4 "faces" of window for each player (does not assign them to the players,
    // note that players must choose their own window
    private void createWindows(){
        windows = new ArrayList<>();
        windows = WindowBuilder.extractWindows(playerIDs.size());
    }

    public void sendWindows(){
        createPrivateObjectives();
        createWindows();
        for(int i=0;i<playerIDs.size();i++) {
            List<Window> windowsToSend = new ArrayList<>();
            for(int j=i*4;j<i*4+4;j++){
                windowsToSend.add(windows.get(j));
            }
            serverView.update(new SetupResponse(
                    playerIDs.get(i),windowsToSend,publicObjectives,privateObjectives.get(i),toolCards,new ArrayList<>(playerNames.values())));
        }
    }

    //when a player send the map he chose
    public void update(Message message){
        SetupMessage setupMessage = (SetupMessage) message;
        players.add(new Player(playerNames.get(setupMessage.getPlayerID()),setupMessage.getPlayerID(),setupMessage.getWindow(),privateObjectives.get(setupsCompleted)));
        setupsCompleted++;
        if(setupsCompleted==playerIDs.size()) createMVC();
    }
}
