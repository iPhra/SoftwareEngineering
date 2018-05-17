package it.polimi.se2018.network.connections.rmi;

import it.polimi.se2018.network.connections.ServerConnection;
import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.view.ServerView;

import java.rmi.RemoteException;

public class RMIServerConnection implements ServerConnection {
    private final RemoteView client;
    private ServerView serverView;

    public RMIServerConnection(RemoteView client) {
        this.client = client;
    }

    public RemoteView getClient() {
        return client;
    }

    @Override
    public void sendResponse(Response response) throws RemoteException {
        client.handleNetworkInput(response);
    }

    @Override
    public void setServerView(ServerView serverView) {
        this.serverView = serverView;
    }
}
