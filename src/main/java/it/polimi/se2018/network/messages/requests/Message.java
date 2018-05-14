package it.polimi.se2018.network.messages.requests;

import java.io.Serializable;

/**
 * This is the abstract class representing a request from the client to the server
 * For more details, check Visitor pattern
 * @author Francesco Lorenzo
 */
public abstract class Message implements Serializable {
    /**
     * This is the id of the player making a request
     */
    private final int playerID;

    protected Message(int playerID) {
        this.playerID = playerID;
    }

    /**
     * Gets the id of the player associated to the message
     * @return the id of the player making the request
     */
    public int getPlayerID() {
        return playerID;
    }

    /**
     * Uses the handler to handle this specific request
     * Implemented by each specific Message subclass
     * @param handler is the object who will handle this request
     */
    public abstract void handle(MessageHandler handler);
}
