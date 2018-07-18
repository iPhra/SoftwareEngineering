package it.polimi.se2018.network.messages.responses;

import java.io.Serializable;

/**
 * This is the abstract class representing a response from the Server to the Client
 * For more details, check Visitor pattern
 */
public abstract class Response implements Serializable {

    /**
     * This is the ID of the player this message will be sent to, or in case of ModelViewResponse it is the current player
     */
    private final int playerID;

    protected Response(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public abstract void handleClass(ResponseHandler responseHandler);
}
