package it.polimi.se2018.network.connections.rmi;

import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.Response;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteConnection extends Remote{

    void getResponse(Response response) throws RemoteException;
    void getMessage(Message message) throws RemoteException;
    void ping() throws RemoteException;
}
