package it.polimi.se2018.network.connections;

import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.view.ServerView;

public class RMIClientConnection implements ClientConnection {
    private final ServerView server;

    RMIClientConnection(ServerView server) {
        this.server=server;
    }

    public ServerView getServer() {
        return server;
    }

    @Override
    public void sendMessage(Message message) {
        server.handleNetworkInput(message);
    }

    @Override
    public void receiveResponse(Response response) {
        //RMI doesn't need to receive messages from the network
    }
}
