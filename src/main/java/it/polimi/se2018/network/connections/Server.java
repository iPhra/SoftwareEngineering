package it.polimi.se2018.network.connections;

import it.polimi.se2018.view.ServerView;

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
        registry.rebind("Server view", UnicastRemoteObject.exportObject(serverView,0));
    }
}
