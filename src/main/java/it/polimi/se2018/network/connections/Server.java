package it.polimi.se2018.network.connections;

import it.polimi.se2018.view.ClientView;
import it.polimi.se2018.view.ServerView;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    ServerView serverView;

    public void createRMIConnection () throws RemoteException, NotBoundException{
        Registry registry = LocateRegistry.getRegistry();
        ClientView client = (ClientView) registry.lookup("Client view");
        ServerConnection connection = new RMIServerConnection(client);
        //settare connection alla view del client
    }

    public void createRMIRegistry () throws RemoteException{
        Registry registry = LocateRegistry.getRegistry();
        registry.rebind("Server view", UnicastRemoteObject.exportObject(serverView,0));
    }
}
