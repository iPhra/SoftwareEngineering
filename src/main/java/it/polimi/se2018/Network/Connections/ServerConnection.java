package it.polimi.se2018.Network.Connections;

import it.polimi.se2018.Network.Messages.Requests.Message;
import it.polimi.se2018.Network.Messages.Responses.Response;

import java.rmi.RemoteException;

public interface ServerConnection {

    public void sendResponse(Response response) throws RemoteException;
    public void receiveMessage(Message message);
}
