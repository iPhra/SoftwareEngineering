package it.polimi.se2018.network.messages.responses;


public class ToolCardResponse extends Response{

    public ToolCardResponse(int player) {
        super(player);
    }

    public void handle(ResponseHandler responseHandler) {responseHandler.handleResponse(this);}
}
