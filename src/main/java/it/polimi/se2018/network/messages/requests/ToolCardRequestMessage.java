package it.polimi.se2018.network.messages.requests;

import it.polimi.se2018.mvc.model.Board;

/**
 * This class represents a message from a player wishing to select a Tool Card
 * @author Francesco Lorenzo
 */
public class ToolCardRequestMessage extends Message {
    /**
     * This is the number of the Tool Card as displayed on the array in {@link Board}
     */
    private final int toolCardNumber;

    public ToolCardRequestMessage(int playerID, int stateID, int toolCardNumber) {
        super(playerID,stateID);
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
