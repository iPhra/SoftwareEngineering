package it.polimi.se2018.client.view;

import it.polimi.se2018.client.network.ClientConnection;
import it.polimi.se2018.network.messages.responses.Response;

import java.io.Serializable;

public class GUIClientView implements ClientView, Serializable {

    @Override
    public void handleNetworkInput(Response response) { //to be implemented
    }

    @Override
    public void setClientConnection(ClientConnection clientConnection) { //to be implemented
    }
}