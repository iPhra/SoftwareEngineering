package it.polimi.se2018.network.messages.responses;

public class TurnStartResponse extends Response {

    public TurnStartResponse(int player) {
        super(player);
    }

    public void handle(ResponseHandler responseHandler) {
        responseHandler.handleResponse(this);
    }


}
