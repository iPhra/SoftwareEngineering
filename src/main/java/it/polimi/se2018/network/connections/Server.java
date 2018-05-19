package it.polimi.se2018.network.connections;

import it.polimi.se2018.controller.GameManager;
import it.polimi.se2018.network.connections.rmi.RMIManager;
import it.polimi.se2018.network.connections.rmi.RemoteManager;
import it.polimi.se2018.network.connections.socket.SocketHandler;
import it.polimi.se2018.utils.DeckBuilder;
import it.polimi.se2018.view.ServerView;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static final int PORT = 1234;
    private static RemoteManager remoteManager;
    private static int matchID = 0;
    private static int playerNumber = 0; //identifies just the player, without the match
    private Map<Integer, GameManager> matches;
    private ServerSocket serverSocket;
    private static Registry registry;
    private DeckBuilder deckBuilder;

    private Server() {
        remoteManager = new RMIManager(this);
        deckBuilder = DeckBuilder.instance();
        matches = new HashMap<>();
    }

    public void setPlayer(int playerID, String playerName, ServerConnection serverConnection) {
        int match = playerID/1000;
        GameManager manager;
        if(matches.get(match)==null) {
            manager = new GameManager(deckBuilder);
            matches.put(match,manager);
            manager.setServerView(new ServerView());
            try {
                registry.rebind("RemoteView", UnicastRemoteObject.exportObject(manager.getServerView(),0));
            }
            catch (RemoteException e) {
                System.err.println(e.getMessage());
            }
        }
        else manager = matches.get(match);
        manager.addPlayerName(playerID,playerName);
        manager.addServerConnection(playerID, serverConnection);
        manager.addPlayerID(playerID);
        serverConnection.setServerView(manager.getServerView());
        manager.getServerView().addServerConnection(playerID,serverConnection);
    }

    public boolean checkName(int playerID, String playerName) {
        return !(matches.get(playerID/1000)==null || matches.get(playerID/1000).checkName(playerName));
    }

    private static void incrementMatchID() {matchID++;}

    private static void incrementPlayerID() {playerNumber++;}

    private boolean isMatchFull() {
        return matches.get(matchID)==null || matches.get(matchID).playersNumber()==4; //oppure timer è scaduto?
    }

    public int generateID() {
        if (isMatchFull()) incrementMatchID();
        incrementPlayerID();
        return (matchID*1000)+playerNumber;
    }

    private void createRMIRegistry () throws RemoteException{
        registry = LocateRegistry.createRegistry(1099);
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
        new PrintStream(System.out).println("Listening...");
        server.startSocketConnection();
        server.createRMIRegistry();
    }
}
