package it.polimi.se2018.Model.Moves;

import it.polimi.se2018.Model.Player;

public class Message {
    private final Player player;

    public Message(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
