package it.polimi.se2018.network.messages.requests;

import it.polimi.se2018.model.Player;

/**
 * This is the abstract class representing a request from the client to the server
 * For more details, check Visitor pattern
 * @author Francesco Lorenzo
 */
public abstract class Message {
    /**
     * This is the player making a request
     */
    private final Player player;

    protected Message(Player player) {
        this.player = player;
    }

    /**
     * Gets the player associated to the message
     * @return the player making the request
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Uses the handler to handle this specific request
     * Implemented by each specific Message subclass
     * @param handler is the object who will handle this request
     */
    public abstract void handle(MessageHandler handler);
}
