package it.polimi.se2018.Network.Connections;

import it.polimi.se2018.View.ClientView;
import it.polimi.se2018.View.ServerView;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Client {
    ClientView clientView;

    public void createRMIConnection() throws RemoteException, NotBoundException{
        Registry registry = LocateRegistry.getRegistry();
        ServerView server = (ServerView) registry.lookup("Server View");
        ClientConnection connection = new RMIClientConnection(server);
        //settare connection alla view del client
    }

    public void createRMIRegistry() throws RemoteException{
        Registry registry = LocateRegistry.getRegistry();
        registry.rebind("Client View", UnicastRemoteObject.exportObject(clientView,0));
    }

}
