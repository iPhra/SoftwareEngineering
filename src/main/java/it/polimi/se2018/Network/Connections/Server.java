package it.polimi.se2018.Network.Connections;

import it.polimi.se2018.View.ServerView;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    ServerView serverView;

    public void createRMIConnection () {
    }

    public void createRMIRegistry () throws RemoteException{
        Registry registry = LocateRegistry.getRegistry();
        registry.rebind("Server View", UnicastRemoteObject.exportObject(serverView,0));
    }
}
