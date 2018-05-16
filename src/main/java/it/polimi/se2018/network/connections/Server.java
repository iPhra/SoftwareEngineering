package it.polimi.se2018.network.connections;

import it.polimi.se2018.network.connections.rmi.RMIManager;
import it.polimi.se2018.network.connections.rmi.RemoteManager;
import it.polimi.se2018.network.connections.rmi.RemoteView;
import it.polimi.se2018.view.ServerView;

import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class Server {
    private RemoteManager remoteManager;
    private RemoteView remoteView;
    private static int matchID = 0;
    private static int playerNumber = 0; //identifies just the player, without the matc
    private Map<Integer,Integer> matches; //maps id of the match to its playerNumber
    private Map<Integer,ServerConnection> serverConnections; //maps playerID to its connection
    private Map<Integer, String> playerNames; //maps playerID to its name
    ServerView serverView = new ServerView();
    private ServerSocket serverSocket;

    private Server() {
        remoteView = new ServerView();
        remoteManager = new RMIManager(this, (ServerView) remoteView);
        matches = new HashMap<>();
    }

    public void setPlayer(int playerID, String playerName, ServerConnection serverConnection) {
        serverConnections.put(playerID,serverConnection);
        playerNames.put(playerID,playerName);
        matches.put(playerID/1000,playerID%1000);
    }

    //implementare
    public boolean checkName(int playerID, String playername) {
        return false;
    }

    public static int getMatchID() {return matchID;}

    public static void incrementMatchID() {matchID++;}

    public static int getPlayerNumber() {return playerNumber;}

    public static void incrementPlayerID() {
        playerNumber++;}

    public static int generateID() {
        incrementMatchID();
        incrementPlayerID();
        return (Server.getMatchID()*1000)+Server.getPlayerNumber();
    }

    private void createRMIRegistry () throws RemoteException{
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("RemoteManager", UnicastRemoteObject.exportObject(remoteManager,0));
        registry.rebind("RemoteView", UnicastRemoteObject.exportObject(remoteView,0));
    }

    //metodo randevouz

    public static void main(String[] args) throws RemoteException {
        Server server = new Server();
        server.createRMIRegistry();
    }
}
