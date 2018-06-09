package it.polimi.se2018.network.messages.requests;

/**
 * This class represents a message from a player wishing to end his turn
 */
public class PassMessage extends Message {
    private final boolean halt;

    public PassMessage(int playerID, int stateID, boolean halt) {
        super(playerID,stateID);
        this.halt = halt;
    }

    public boolean isHalt() {
        return halt;
    }

    /**
     * Uses the handler to handle this specific pass request
     * @param handler is the object who will handle this request
     */
    @Override
    public void handle(MessageHandler handler) {
        handler.handleMove(this);
    }
}
