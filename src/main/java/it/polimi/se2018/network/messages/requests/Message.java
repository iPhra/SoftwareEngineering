package it.polimi.se2018.network.messages.requests;

import java.io.Serializable;

/**
 * This is the abstract class representing a request from the client to the server
 * For more details, check Visitor pattern
 */
public abstract class Message implements Serializable {
    /**
     * This is the id of the player making a request
     */
    private final int playerID;

    /**
     * This is the unique id of the state of the game
     */
    private final int stateID;

    Message(int playerID, int stateID) {
        this.playerID = playerID;
        this.stateID = stateID;
    }

    /**
     * Gets the id of the player associated to the message
     * @return the id of the player making the request
     */
    public int getPlayerID() {
        return playerID;
    }

    /**
     * @return the unique id of the state of the game
     */
    public int getStateID() { return stateID;}

    /**
     * Uses the handler to handle this specific request
     * Implemented by each specific Message subclass
     * @param handler is the object who will handle this request
     */
    public abstract void handle(MessageHandler handler);
}
