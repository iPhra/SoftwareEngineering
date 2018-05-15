package it.polimi.se2018.network.connections.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteManager extends Remote {

    void addClient(int playerID, RemoteView clientView) throws RemoteException;
    int getID() throws RemoteException;
}
