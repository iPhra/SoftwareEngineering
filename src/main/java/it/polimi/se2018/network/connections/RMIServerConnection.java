package it.polimi.se2018.network.connections;

import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.view.ClientView;

import java.rmi.RemoteException;

public class RMIServerConnection implements ServerConnection {
    private final ClientView client;

    public RMIServerConnection(ClientView client) {
        this.client = client;
    }

    public ClientView getClient() {
        return client;
    }

    @Override
    public void sendResponse(Response response) throws RemoteException {
        client.handleNetworkInput(response);
    }

    @Override
    public void receiveMessage(Message message) {
        //RMI doesn't need to receive a message
    }
}
