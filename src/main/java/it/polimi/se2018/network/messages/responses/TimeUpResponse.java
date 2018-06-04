package it.polimi.se2018.network.messages.responses;

import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.network.messages.responses.ResponseHandler;

public class TimeUpResponse extends Response {

    public TimeUpResponse(int playerID) {
        super(playerID);
    }

    @Override
    public void handleClass(ResponseHandler responseHandler) {
        responseHandler.handleResponse(this);
    }
}
