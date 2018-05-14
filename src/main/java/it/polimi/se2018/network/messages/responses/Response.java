package it.polimi.se2018.network.messages.responses;

import java.io.Serializable;

public abstract class Response implements Serializable {
    private final int playerID;

    protected Response(int player) {
        this.playerID = player;
    }

    public int getPlayer() {
        return playerID;
    }

    public abstract void handle(ResponseHandler handler);
}
