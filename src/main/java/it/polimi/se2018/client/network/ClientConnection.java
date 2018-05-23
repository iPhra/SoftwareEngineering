package it.polimi.se2018.client.network;

import it.polimi.se2018.network.messages.requests.Message;

import java.rmi.RemoteException;

public interface ClientConnection {

    void sendMessage(Message message) throws RemoteException;
    void stop();
}
