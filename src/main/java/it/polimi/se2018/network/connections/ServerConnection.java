package it.polimi.se2018.network.connections;

import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.Response;

import java.rmi.RemoteException;

public interface ServerConnection {

    void sendResponse(Response response) throws RemoteException;
    void receiveMessage(Message message);
}
