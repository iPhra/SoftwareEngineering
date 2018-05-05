package it.polimi.se2018.Model.Messages;

import it.polimi.se2018.Model.Player;

public abstract class Message {
    private final Player player;

    public Message(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public abstract void handle(MessageHandler handler);
}
