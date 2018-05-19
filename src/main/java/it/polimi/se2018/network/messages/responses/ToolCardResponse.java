package it.polimi.se2018.network.messages.responses;

/**
 * This class represents an ack from the Server to the Client relative to using a {@link it.polimi.se2018.model.toolcards.ToolCard}
 */
public class ToolCardResponse extends Response{
    /**
     * This is the number of the {@link it.polimi.se2018.model.toolcards.ToolCard} to be used
     */
    private final int toolCardNumber;

    public ToolCardResponse(int player, int toolCardNumber) {
        super(player);
        this.toolCardNumber=toolCardNumber;
    }

    /**
     * @return the number of the {@link it.polimi.se2018.model.toolcards.ToolCard} to be used
     */
    public int getToolCardNumber() {
        return toolCardNumber;
    }

    /**
     * Uses the handler to handle this specific Tool Card response
     * @param responseHandler is the object who will handle this response
     */
    @Override
    public void handle(ResponseHandler responseHandler) {responseHandler.handleResponse(this);}
}
