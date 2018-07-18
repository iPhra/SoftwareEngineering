package it.polimi.se2018.network;
import it.polimi.se2018.mvc.controller.GameManager;
import it.polimi.se2018.network.connections.ServerConnection;
import it.polimi.se2018.network.connections.rmi.RMIManager;
import it.polimi.se2018.network.connections.socket.SocketHandler;
import it.polimi.se2018.utils.Stopper;
import it.polimi.se2018.utils.WaitingThread;
import it.polimi.se2018.mvc.view.ServerView;

import java.io.*;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the Server class functioning as an entry point to the server-side
 */
@SuppressWarnings("FieldCanBeLocal")
public class Server implements Stopper {
    private static final int PORT = 1234;

    /**
     * This is the unique ID of every match, startin from 1
     */
    private static int matchID = 1;

    /**
     * This identifies the number of the player, it's part of the identifier of a player, which is made by matchID + playerNumber
     */
    private static int playerNumber = 0;

    /**
     * This maps each matchID to its GameManager
     */
    private final Map<Integer, GameManager> matches;

    /**
     * This maps each nickname to its playerID, a nickname is unique FOR ALL MATCHES! It's a username
     */
    private final Map<String, Integer> nicknames;
    private WaitingThread clock;

    /**
     * This is the manager of RMI connections
     */
    private RMIManager remoteManager;

    /**
     * This is the manager of Socket connections
     */
    private SocketHandler socketHandler;
    private Duration timeout;

    public Server() {
        getDuration();
        matches = new HashMap<>();
        nicknames = new HashMap<>();
    }

    /**
     * This method reads the duration of the timer from a configuration file
     */
    private void getDuration() {
        InputStream in = getClass().getClassLoader().getResourceAsStream("TimerProperties.txt");
        try(BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String[] tokens = sb.toString().split(";");
            timeout = Duration.ofSeconds(Integer.parseInt(tokens[0].split(":")[1]));
        }
        catch (IOException e) {
            timeout = Duration.ofSeconds(60);
        }
    }

    /**
     * This method launches the thread that wakes up after the timer has ran out
     */
    private void startTimer() {
        clock = new WaitingThread(timeout, this);
        clock.start();
    }

    /**
     * This method is called when either the timer of the lobby has ran out or when 4 players connected
     * @param manager is the manager of the current match
     */
    private void startGame(GameManager manager) {
        clock.interrupt();
        incrementMatchID();
        manager.sendWindows();
    }

    /**
     * This method creates the Game Manager associated to a match id
     * @param match is the id of the match
     * @return the new Game Manager
     */
    private GameManager createManager(int match) {
        GameManager manager = new GameManager(this);
        manager.setServerView(new ServerView());
        manager.startSetup();
        matches.put(match,manager);
        return manager;
    }

    /**
     * Adds a player who just connected to the server to the available match
     * @param manager is the manager of the match
     * @param playerID is the id of the player to add
     * @param playerName is the nickname of the player to add
     * @param serverConnection is the connection of the player to add
     */
    private void addPlayer(GameManager manager, int playerID, String playerName, ServerConnection serverConnection) {
        manager.addPlayerName(playerID,playerName);
        manager.addServerConnection(playerID, serverConnection);
        manager.addPlayerID(playerID);
        serverConnection.setServerView(manager.getServerView());
        manager.getServerView().addServerConnection(playerID,serverConnection);
    }

    /**
     * Removes a player who disconnected during lobby selection, stops the timer if less than 2 players are in the lobby
     * @param playerID is the id of the player who disconnected
     */
    private void removePlayer(int playerID) {
        int match = playerID/1000; //this is the ID of the match
        GameManager manager = matches.get(match);
        if(manager.playersNumber()==2) {
            clock.interrupt();
        }
        nicknames.remove(manager.getNicknameById(playerID));
        manager.removePlayer(playerID);
    }

    private static void incrementMatchID() {matchID++;}

    /**
     * @return {@code true} if 4 players connected
     */
    private boolean isMatchFull() {
        return matches.get(matchID).playersNumber()==4;
    }

    private void createRMIRegistry () {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            remoteManager = new RMIManager(this,registry);
            registry.rebind("RemoteManager", UnicastRemoteObject.exportObject(remoteManager,0));
        }
        catch (RemoteException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

    private void startSocketConnection(){
        try{
            ServerSocket serverSocket = new ServerSocket(PORT);
            socketHandler = new SocketHandler(this,serverSocket);
            new Thread(socketHandler).start();
        }
        catch(IOException e){
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

    public int getPlayerID(String nickname) {
        return nicknames.get(nickname);
    }

    /**
     * Adds a player to a game, if one doesn't exists it creates a game and adds the player to it
     * @param playerID is the id of the player who just connected
     * @param nickname is the nickname of the player who just connected
     * @param serverConnection is the server of the player who just connected
     */
    public void setPlayer(int playerID, String nickname, ServerConnection serverConnection) {
        nicknames.put(nickname,playerID);
        int match = playerID/1000;
        GameManager manager;
        if(matches.get(match)==null) manager = createManager(match);
        else manager = matches.get(match);
        addPlayer(manager,playerID,nickname,serverConnection);
        if (manager.playersNumber() == 2) startTimer();
        if (isMatchFull()) startGame(manager);
    }

    /**
     * Reconnects a player to its match
     * @param playerID is the id of the player reconnecting
     * @param serverConnection is the server connection of the player
     */
    public synchronized void handleReconnection(int playerID, ServerConnection serverConnection) {
        GameManager manager = matches.get(playerID/1000);
        manager.setReconnected(playerID, serverConnection);
    }

    /**
     * Disconnects a player from its game
     * @param playerID is the id of the player disconnecting
     */
    public synchronized void handleDisconnection(int playerID) {
        int match = playerID / 1000;
        GameManager manager = matches.get(match);
        if (manager != null) {
            if (manager.isMatchCreated()) manager.setDisconnected(playerID);
            else {
                ServerConnection connection = manager.getServerConnection(playerID);
                removePlayer(playerID);
                connection.stop();
            }
        }
    }

    /**
     * Checks if a nickname is associated to a player who is disconnected
     * @param playerName is the nickname of the player
     * @return {@code true} if the nickname is associated to a player who is disconnected
     */
    public boolean checkDisconnection(String playerName) {
        return matches.get(nicknames.get(playerName)/1000).isDisconnected(nicknames.get(playerName));
    }

    /**
     * Checks if the nickname is already taken by a user
     * @param nickname is the nickname to check
     * @return {@code true} if the nickname is not associated to any user
     */
    public boolean checkRegistration(String nickname) {
        return !nicknames.containsKey(nickname);
    }

    /**
     * Removes a player from the registered users
     * @param nickname is the name of the player
     */
    public void deregisterPlayer(String nickname) {
        nicknames.remove(nickname);
    }

    /**
     * Called when a match ends
     * @param matchID is the id of the match
     */
    public void deregisterMatch(int matchID) {
        matches.remove(matchID);
    }

    /**
     * @return a new ID of a player based on the match he's in
     */
    public static int generateID() {
        playerNumber++;
        return (matchID*1000)+playerNumber;
    }

    @Override
    public void halt(String message) {
        if (matches.get(matchID).playersNumber() >= 2) {
            matches.get(matchID).sendWindows();
            incrementMatchID();
        }
    }


    public static void main(String[] args) {
        Server server = new Server();
        server.startSocketConnection();
        server.createRMIRegistry();
        new PrintStream(System.out).println("Listening...");
    }
}
