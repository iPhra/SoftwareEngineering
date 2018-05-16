package it.polimi.se2018.network.messages.responses;


public class ToolCardResponse extends Response{
    private final int toolCardNumber;

    public ToolCardResponse(int player, int toolCardNumber) {
        super(player);
        this.toolCardNumber=toolCardNumber;
    }

    public void handle(ResponseHandler responseHandler) {responseHandler.handleResponse(this);}

    public int getToolCardNumber() {
        return toolCardNumber;
    }
}
