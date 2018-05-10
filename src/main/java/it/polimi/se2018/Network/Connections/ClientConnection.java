package it.polimi.se2018.Network.Connections;

import it.polimi.se2018.Network.Messages.Requests.Message;
import it.polimi.se2018.Network.Messages.Responses.Response;

import java.rmi.RemoteException;

public interface ClientConnection {

    public void sendMessage(Message message) throws RemoteException;
    public void receiveResponse(Response response);
}
