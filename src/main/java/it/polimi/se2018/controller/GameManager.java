package it.polimi.se2018.controller;

import it.polimi.se2018.model.Board;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.objectives.privateobjectives.PrivateObjective;
import it.polimi.se2018.network.connections.ServerConnection;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.utils.DeckBuilder;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.view.ServerView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManager implements Runnable,Observer<Message>{
    private DeckBuilder deckBuilder;
    private ServerView serverView;
    private Board board;
    private Controller controller;
    private List<Integer> playerIDs;
    private Map<Integer,ServerConnection> serverConnections; //maps playerID to its connection
    private Map<Integer, String> playerNames; //maps playerID to its name
    private List<Player> players;
    private List<PrivateObjective> privateObjectives;

    public GameManager(DeckBuilder deckBuilder){
        this.deckBuilder = deckBuilder;
        this.playerIDs = new ArrayList<>();
        this.serverConnections = new HashMap<>();
        this.playerNames = new HashMap<>();
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

    public void run(){
        startGame();
    }

    private void startGame(){
        controller = new Controller();
        controller.register(this);
        createPrivateObjectives();
        //costruisce i pubblici
        //costruisce le toolcard
        //invia tutto
    }

    //rinominalo generatePrivateObjectives();
    //assign privateObjective to every player
    private void createPrivateObjectives() {
        privateObjectives = new ArrayList<>();
        privateObjectives = deckBuilder.extractPrivateObjectives(playerIDs.size());

    }

    //create the list of Player of the game
    private void createListOfPlayer() {
        players = new ArrayList<>();
        for (Integer playerID : playerNames.keySet()) {
            //players.add(new Player(playerNames.get(playerID), playerID, window, privateObjective));
        }
    }

    private void sendWindows(){

    }

    public void update(Message message){
    }
}
