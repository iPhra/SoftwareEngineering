package it.polimi.se2018.network.messages.requests;

/**
 * This class represents a message from a player wishing to end his turn
 * @author Francesco Lorenzo
 */
public class PassMessage extends Message {

    public PassMessage(int player) {
        super(player);
    }

    /**
     * Uses the handler to handle this specific pass request
     * @param handler is the object who will handle this request
     */
    @Override
    public void handle(MessageHandler handler) {
        handler.performMove(this);
    }
}
