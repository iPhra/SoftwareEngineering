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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Stopper {
    private static final int PORT = 1234;
    private static int matchID = 1;
    private static int playerNumber = 0; //identifies just the player, without the match
    private final Map<Integer, GameManager> matches;
    private final Map<String, Integer> nicknames;
    private WaitingThread clock;
    private RMIManager remoteManager;
    private SocketHandler socketHandler;

    private Server() {
        matches = new HashMap<>();
        nicknames = new HashMap<>();
    }

    private void startTimer() {
        Duration timeout = Duration.ofSeconds(10);
        clock = new WaitingThread(timeout, this);
        clock.start();
    }

    private void startGame(GameManager manager) {
        clock.interrupt();
        incrementMatchID();
        manager.sendWindows();
    }

    private GameManager createManager(int match) {
        GameManager manager = new GameManager();
        manager.setServerView(new ServerView());
        manager.startSetup();
        matches.put(match,manager);
        return manager;
    }

    private void addPlayer(GameManager manager, int playerID, String playerName, ServerConnection serverConnection) {
        manager.addPlayerName(playerID,playerName);
        manager.addServerConnection(playerID, serverConnection);
        manager.addPlayerID(playerID);
        serverConnection.setServerView(manager.getServerView());
        manager.getServerView().addServerConnection(playerID,serverConnection);
    }

    private void removePlayer(int playerID) {
        int match = playerID/1000;
        GameManager manager = matches.get(match);
        if(manager.playersNumber()==2) {
            clock.interrupt();
        }
        manager.removePlayer(playerID);
    }

    private static void incrementMatchID() {matchID++;}

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
            Thread thread = new Thread(socketHandler);
            thread.start();
        }catch(IOException e){
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

    private void close() {
        socketHandler.stop();
        remoteManager.closeConnection();
    }

    public int getPlayerID(String nickname) {
        return nicknames.get(nickname);
    }

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

    public synchronized void handleReconnection(int playerID, ServerConnection serverConnection) {
        GameManager manager = matches.get(playerID/1000);
        manager.setReconnected(playerID, serverConnection);
    }

    public synchronized void handleDisconnection(int playerID) {
        int match = playerID/1000;
        GameManager manager = matches.get(match);
        if(manager.isMatchPlaying()) {
            manager.setDisconnected(playerID);
        }
        else {
            ServerConnection connection = manager.getServerConnection(playerID);
            removePlayer(playerID);
            connection.stop();
        }
    }

    public boolean checkDisconnection(String playerName) {
        return matches.get(nicknames.get(playerName)/1000).isDisconnected(nicknames.get(playerName));
    }

    public boolean checkRegistration(String nickname) {
        return !nicknames.containsKey(nickname);
    }

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
        Scanner scanner = new Scanner(System.in);
        while(!scanner.nextLine().startsWith("exit"));
        server.close();
    }
}
