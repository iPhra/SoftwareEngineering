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
    private static int uniqueID;
    private Map<Integer, ServerConnection> serverConnections;
    ServerView serverView = new ServerView();
    private ServerSocket serverSocket;
    private ExecutorService pool;
    private final boolean isOpen = true;

    private Server() {
        remoteView = new ServerView();
        remoteManager = new RMIManager(this, (ServerView) remoteView);
        serverConnections = new HashMap<>();
    }

    public void setServerConnections(int playerID, ServerConnection serverConnection) {
        serverConnections.put(playerID,serverConnection);
    }

    public static int getUniqueID() {return uniqueID;}

    public static void incrementUniqueID() {uniqueID++;}

    private void createRMIRegistry () throws RemoteException{
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("RemoteManager", UnicastRemoteObject.exportObject(remoteManager,0));
        registry.rebind("RemoteView", UnicastRemoteObject.exportObject(remoteView,0));
    }

    //metodi per creare Tool Cards, Private Objectives, Public Objectives (classe deck?)

    //metodo randevouz

    public static void main(String[] args) throws RemoteException {
        Server server = new Server();
        server.createRMIRegistry();
    }
}
