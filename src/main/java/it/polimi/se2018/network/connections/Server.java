package it.polimi.se2018.network.connections;

import it.polimi.se2018.controller.GameManager;
import it.polimi.se2018.network.connections.rmi.RMIManager;
import it.polimi.se2018.network.connections.rmi.RemoteManager;
import it.polimi.se2018.network.connections.socket.SocketHandler;
import it.polimi.se2018.utils.DeckBuilder;
import it.polimi.se2018.utils.Timing;
import it.polimi.se2018.utils.WaitingThread;
import it.polimi.se2018.view.ServerView;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Server implements Timing {
    private static final int PORT = 1234;
    private static int matchID = 1;
    private static int playerNumber = 0; //identifies just the player, without the match
    private Map<Integer, GameManager> matches;
    private ServerSocket serverSocket;
    private DeckBuilder deckBuilder;
    private Registry registry;
    WaitingThread clock;

    private Server() {
        deckBuilder = DeckBuilder.instance();
        matches = new HashMap<>();
        Duration timeout = Duration.ofSeconds(60);
        clock = new WaitingThread(timeout, this);
    }

    public void setPlayer(int playerID, String playerName, ServerConnection serverConnection) {
        int match = playerID/1000;
        GameManager manager;
        if(matches.get(match)==null) {
            manager = new GameManager(deckBuilder);
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
        if (manager.playersNumber() == 2) {
            Duration timeout = Duration.ofSeconds(60);
            clock = new WaitingThread(timeout, this);
            clock.start();
        }
        if (isMatchFull()) {
            incrementMatchID();
            manager.sendWindows();
            clock.interrupt();
        }
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

    private void createRMIRegistry () throws RemoteException{
        registry = LocateRegistry.createRegistry(1099);
        RemoteManager remoteManager = new RMIManager(this,registry);
        registry.rebind("RemoteManager", UnicastRemoteObject.exportObject(remoteManager,0));
    }

    private void startSocketConnection(){
        try{
            serverSocket = new ServerSocket(PORT);
            SocketHandler socketHandler = new SocketHandler(this,serverSocket);
            new Thread(socketHandler).start();
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }

    private void closeSocketConnection(){
        try{
            serverSocket.close();
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws RemoteException {
        Server server = new Server();
        server.startSocketConnection();
        server.createRMIRegistry();
        new PrintStream(System.out).println("Listening...");
    }

    @Override
    public void onTimesUp() {
        if (matches.get(matchID).playersNumber() >= 2) {
            matches.get(matchID).sendWindows();
            incrementMatchID();
        }
    }
}
