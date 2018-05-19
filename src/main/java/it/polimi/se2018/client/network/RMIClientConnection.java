package it.polimi.se2018.client.network;

import it.polimi.se2018.client.network.ClientConnection;
import it.polimi.se2018.network.connections.rmi.RemoteView;
import it.polimi.se2018.network.messages.requests.Message;

import java.rmi.RemoteException;

public class RMIClientConnection implements ClientConnection {
    private final RemoteView server;

    public RMIClientConnection(RemoteView server) {
        this.server=server;
    }

    @Override
    public void sendMessage(Message message) throws RemoteException{
        server.handleNetworkInput(message);
    }
}
