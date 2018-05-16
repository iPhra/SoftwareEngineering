package it.polimi.se2018.network.connections.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteManager extends Remote {

    boolean addClient(int playerID, String playerName, RemoteView clientView) throws RemoteException;
    int getID() throws RemoteException;
}
