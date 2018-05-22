package it.polimi.se2018.network.messages.requests;

/**
 * This class represents a message from a player wishing to select a Tool Card
 * @author Francesco Lorenzo
 */
public class ToolCardRequestMessage extends Message {
    /**
     * This is the number of the Tool Card as displayed on the array in {@link it.polimi.se2018.model.Board}
     */
    private final int toolCardNumber;

    public ToolCardRequestMessage(int player, int toolCardNumber) {
        super(player);
        this.toolCardNumber=toolCardNumber;
    }

    /**
     * @return the integer representing the number of the Tool Card you want to use
     */
    public int getToolCardNumber() {
        return toolCardNumber;
    }

    /**
     * Uses the handler to handle this specific Tool Card request
     * @param handler is the object who will handle this request
     */
    @Override
    public void handle(MessageHandler handler) {
        handler.handleMove(this);
    }
}
