package it.polimi.se2018.network.connections;

import it.polimi.se2018.network.connections.RMI.RMIManager;
import it.polimi.se2018.network.connections.RMI.RemoteManager;
import it.polimi.se2018.network.connections.RMI.RemoteView;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.view.ServerView;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    private static RemoteManager remoteManager;
    private static RemoteView remoteView;

    private Server() {
        remoteView = new ServerView();
        remoteManager = new RMIManager((ServerView) remoteView);
    }

    private void createRMIRegistry () throws RemoteException{
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("RemoteManager", UnicastRemoteObject.exportObject(remoteManager,0));
        registry.rebind("RemoteView", UnicastRemoteObject.exportObject(remoteView,0));
    }

    public static void main(String[] args) throws RemoteException {
        Server server = new Server();
        server.createRMIRegistry();
        boolean ciao = true;
        while(ciao) {}
    }
}
