package it.polimi.se2018.network.messages.responses;

import java.io.Serializable;

/**
 * This is the abstract class representing a response from the Server to the Client
 * For more details, check Visitor pattern
 * @author Francesco Lorenzo
 */
public abstract class Response implements Serializable {
    /**
     * This is the ID of the player this message will be sent to, or in case of ModelViewResponse it is the current player
     */
    private final int playerID;
    private String description;

    protected Response(int player) {
        this.playerID = player;
    }

    /**
     * @return the ID of the player this message will be sent to
     */
    public int getPlayer() {
        return playerID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Uses the handler to handle this specific response
     * Implemented by each specific Response subclass
     * @param responseHandler is the object who will handle this response
     */
    public abstract void handle(ResponseHandler responseHandler);
}
