package it.polimi.se2018.network.connections.RMI;

import it.polimi.se2018.network.connections.ServerConnection;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.view.ClientView;

import java.rmi.RemoteException;

public class RMIServerConnection implements ServerConnection {
    private final RemoteView client;

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

}
