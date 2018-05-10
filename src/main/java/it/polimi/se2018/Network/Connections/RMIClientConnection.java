package it.polimi.se2018.Network.Connections;

import it.polimi.se2018.Network.Messages.Requests.Message;
import it.polimi.se2018.Network.Messages.Responses.Response;
import it.polimi.se2018.View.ServerView;

import java.rmi.RemoteException;

public class RMIClientConnection implements ClientConnection {
    private final ServerView server;

    public RMIClientConnection(ServerView server) {
        this.server=server;
    }

    public ServerView getServer() {
        return server;
    }

    @Override
    public void sendMessage(Message message) throws RemoteException {
        server.handleNetworkInput(message);
    }

    @Override
    public void receiveResponse(Response response) {
        //RMI doesn't need to receive messages from the network
    }
}
