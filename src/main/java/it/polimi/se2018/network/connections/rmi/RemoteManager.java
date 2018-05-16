package it.polimi.se2018.network.connections.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteManager extends Remote {

    void addClient(int playerID, String playerName, RemoteView clientView) throws RemoteException;

    int getID() throws RemoteException;

    boolean checkName(int playerID, String playerName) throws RemoteException;
}
