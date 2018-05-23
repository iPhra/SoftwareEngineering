package it.polimi.se2018.network;

import it.polimi.se2018.controller.GameManager;
import it.polimi.se2018.network.connections.ServerConnection;
import it.polimi.se2018.network.connections.rmi.RMIManager;
import it.polimi.se2018.network.connections.rmi.RemoteManager;
import it.polimi.se2018.network.connections.socket.SocketHandler;
import it.polimi.se2018.utils.Timing;
import it.polimi.se2018.utils.WaitingThread;
import it.polimi.se2018.view.ServerView;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Server implements Timing {
    private static final int PORT = 1234;
    private static int matchID = 1;
    private static int playerNumber = 0; //identifies just the player, without the match
    private Map<Integer, GameManager> matches;
    private WaitingThread clock;
    private RMIManager remoteManager;
    private SocketHandler socketHandler;

    private Server() {
        matches = new HashMap<>();
        Duration timeout = Duration.ofSeconds(20);
        clock = new WaitingThread(timeout, this);
    }

    private void startTimer() {
        Duration timeout = Duration.ofSeconds(20);
        clock = new WaitingThread(timeout, this);
        clock.start();
    }

    public void setPlayer(int playerID, String playerName, ServerConnection serverConnection) {
        int match = playerID/1000;
        GameManager manager;
        if(matches.get(match)==null) {
            manager = new GameManager();
            manager.setServerView(new ServerView());
            manager.startSetup();
            matches.put(match,manager);
        }
        else manager = matches.get(match);
        manager.addPlayerName(playerID,playerName);
        manager.addServerConnection(playerID, serverConnection);
        manager.addPlayerID(playerID);
        serverConnection.setServerView(manager.getServerView());
        manager.getServerView().addServerConnection(playerID,serverConnection);
        if (manager.playersNumber() == 2) startTimer();
        if (isMatchFull()) {
            incrementMatchID();
            manager.sendWindows();
            clock.interrupt();
        }
    }

    public void handleDisconnection(int playerID) {
        int match = playerID/1000;
        GameManager manager = matches.get(match);
        if(manager.isMatchPlaying()) {

        }
        else removePlayer(playerID);
    }

    private void removePlayer(int playerID) {
        int match = playerID/1000;
        GameManager manager = matches.get(match);
        if(manager.playersNumber()==2) clock.interrupt();
        manager.removePlayer(playerID);
    }

    public boolean checkName(int playerID, String playerName) {
        return !(matches.get(playerID/1000)==null || matches.get(playerID/1000).checkName(playerName));
    }

    private static void incrementMatchID() {matchID++;}

    private static void incrementPlayerID() {playerNumber++;}

    private boolean isMatchFull() {
        return matches.get(matchID).playersNumber()==4;
    }

    public int generateID() {
        incrementPlayerID();
        return (matchID*1000)+playerNumber;
    }

    private void createRMIRegistry () {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            remoteManager = new RMIManager(this,registry);
            registry.rebind("RemoteManager", UnicastRemoteObject.exportObject(remoteManager,0));
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void startSocketConnection(){
        try{
            ServerSocket serverSocket = new ServerSocket(PORT);
            socketHandler = new SocketHandler(this,serverSocket);
            Thread thread = new Thread(socketHandler);
            thread.start();
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void wakeUp() {
        if (matches.get(matchID).playersNumber() >= 2) {
            matches.get(matchID).sendWindows();
            incrementMatchID();
        }
    }

    private void close() {
        socketHandler.stop();
        remoteManager.closeConnection();
    }



    public static void main(String[] args) {
        Server server = new Server();
        server.startSocketConnection();
        server.createRMIRegistry();
        new PrintStream(System.out).println("Listening...");
    }
}
