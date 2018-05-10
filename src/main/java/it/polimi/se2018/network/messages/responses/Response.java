package it.polimi.se2018.network.messages.responses;

import it.polimi.se2018.model.Player;

public abstract class Response {
    private final Player player;

    protected Response(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public abstract void handle(ResponseHandler handler);
}
