package it.polimi.se2018.network.messages.responses;

public class TurnStartResponse extends Response {

    public TurnStartResponse(int player) {
        super(player);
    }

    /**
     * Uses the handler to handle this specific notification response
     * @param responseHandler is the object who will handle this response
     */
    @Override
    public void handle(ResponseHandler responseHandler) {
        responseHandler.handleResponse(this);
    }


}
