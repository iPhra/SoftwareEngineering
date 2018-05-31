package it.polimi.se2018.network.messages.responses;

import java.io.Serializable;

public abstract class Response implements Serializable{
    /**
     * This is the ID of the player this message will be sent to, or in case of ModelViewResponse it is the current player
     */
    private final int playerID;

    public Response(int playerID) {
        this.playerID = playerID;
    }

    /**
     * @return the ID of the player this message will be sent to
     */
    public int getPlayer() {
        return playerID;
    }

    public abstract void handleClass(ResponseHandler responseHandler);
}
