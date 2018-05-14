package it.polimi.se2018.network.connections;

import it.polimi.se2018.view.ServerView;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    ServerView serverView = new ServerView(null);

    public void createRMIConnection () throws RemoteException, NotBoundException{
    }

    public void createRMIRegistry () throws RemoteException{
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("ServerView", UnicastRemoteObject.exportObject(serverView,0));
    }

    public static void main(String[] args) throws RemoteException {
        Server server = new Server();
        server.createRMIRegistry();
        boolean ciao = true;
        while(ciao) {}
    }
}
