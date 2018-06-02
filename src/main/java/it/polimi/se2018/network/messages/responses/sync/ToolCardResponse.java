package it.polimi.se2018.network.messages.responses.sync;

import it.polimi.se2018.mvc.model.toolcards.ToolCard;

/**
 * This class represents an ack from the Server to the Client relative to using a {@link ToolCard}
 */
public class ToolCardResponse extends SyncResponse {
    /**
     * This is the value of the {@link ToolCard} to be used
     */
    private final int toolCardNumber;

    public ToolCardResponse(int player, int toolCardNumber) {
        super(player);
        this.toolCardNumber=toolCardNumber;
    }

    /**
     * @return the value of the {@link ToolCard} to be used
     */
    public int getToolCardNumber() {
        return toolCardNumber;
    }

    /**
     * Uses the handler to handle this specific Tool Card response
     * @param syncResponseHandler is the object who will handle this response
     */
    @Override
    public void handle(SyncResponseHandler syncResponseHandler) {
        syncResponseHandler.handleResponse(this);}
}
