package it.polimi.se2018.network.connections;

import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.Response;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteView extends Remote{

    void handleNetworkInput(Message message) throws RemoteException;
    void handleNetworkInput(Response response) throws RemoteException;
}
