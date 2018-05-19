package it.polimi.se2018.client.view;

import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.Response;

import java.io.Serializable;

public class GUIClientView implements ClientView, Serializable {

    @Override
    public void handleNetworkInput(Response response) {
    }

    @Override
    public void handleNetworkInput(Message message) {
    }
}
