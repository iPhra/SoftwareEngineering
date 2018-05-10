package it.polimi.se2018.Network.Connections;

import it.polimi.se2018.Network.Messages.Requests.Message;
import it.polimi.se2018.Network.Messages.Responses.Response;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteView extends Remote{

    public void handleNetworkInput(Message message) throws RemoteException;
    public void handleNetworkInput(Response response) throws RemoteException;
}
