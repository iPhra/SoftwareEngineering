package it.polimi.se2018.network.connections;

import it.polimi.se2018.network.connections.RMI.RMIManager;
import it.polimi.se2018.network.connections.RMI.RemoteManager;
import it.polimi.se2018.network.connections.RMI.RemoteView;
import it.polimi.se2018.view.ServerView;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static RemoteManager remoteManager;
    private static RemoteView remoteView;
    ServerView serverView = new ServerView();
    private ServerSocket serverSocket;
    private ExecutorService pool;
    private final boolean isOpen = true;

    private Server() {
        remoteView = new ServerView();
        remoteManager = new RMIManager((ServerView) remoteView);
    }

    private void createRMIRegistry () throws RemoteException{
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("RemoteManager", UnicastRemoteObject.exportObject(remoteManager,0));
        registry.rebind("RemoteView", UnicastRemoteObject.exportObject(remoteView,0));
    }

    public void startSocketServerConnection(int port){
        try{
            serverSocket = new ServerSocket(port);
            pool = Executors.newCachedThreadPool();
            SocketHandler socketHandler = new SocketHandler(this,serverSocket, pool, serverView);
            socketHandler.run();

        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }

    public void closeSocketServerConnection(){
        try{
            serverSocket.close();
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
        pool.shutdown();
    }

    public static void main(String[] args) throws RemoteException {
        Server server = new Server();
        //server.createRMIRegistry();
        boolean ciao = true;
        server.startSocketServerConnection(1234);

    }
}
