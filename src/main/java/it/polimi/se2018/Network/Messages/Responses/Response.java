package it.polimi.se2018.Network.Messages.Responses;

import it.polimi.se2018.Model.Player;

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
