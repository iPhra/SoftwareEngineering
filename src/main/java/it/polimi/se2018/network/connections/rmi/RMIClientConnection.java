package it.polimi.se2018.network.connections.rmi;

import it.polimi.se2018.network.connections.ClientConnection;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.Response;

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
