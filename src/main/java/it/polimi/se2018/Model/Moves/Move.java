package it.polimi.se2018.Model.Moves;

import it.polimi.se2018.Model.Player;

public abstract class Move { //every specific move will inherit from this class
    protected Player player;

    protected Move(Player player) {
        this.player = player;
    }

    public Player getPlayer () { return player; }
}
