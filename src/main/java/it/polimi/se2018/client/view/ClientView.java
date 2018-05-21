package it.polimi.se2018.client.view;

import it.polimi.se2018.client.network.ClientConnection;
import it.polimi.se2018.network.messages.responses.Response;

public interface ClientView {

    void handleNetworkInput(Response response);
    void setClientConnection(ClientConnection clientConnection);
}
