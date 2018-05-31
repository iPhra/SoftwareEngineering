package it.polimi.se2018.network.messages.responses;

import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.network.messages.responses.ResponseHandler;

public class TurnEndResponse extends Response {

    public TurnEndResponse(int playerID) {
        super(playerID);
    }

    @Override
    public void handleClass(ResponseHandler responseHandler) {
        responseHandler.handleResponse(this);
    }
}
