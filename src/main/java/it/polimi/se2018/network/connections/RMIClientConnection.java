package it.polimi.se2018.network.connections;

import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.Response;

import java.rmi.RemoteException;

public class RMIClientConnection implements ClientConnection {
    private final RemoteView server;

    RMIClientConnection(RemoteView server) {
        this.server=server;
    }

    public RemoteView getServer() {
        return server;
    }

    @Override
    public void sendMessage(Message message) throws RemoteException{
        server.handleNetworkInput(message);
    }

    @Override
    public void receiveResponse(Response response) {
        //RMI doesn't need to receive messages from the network
    }
}
