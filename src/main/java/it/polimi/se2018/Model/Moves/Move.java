package it.polimi.se2018.Model.Moves;

import it.polimi.se2018.Model.Player;

public abstract class Move { //every specific move will inherit from this class
    private Player player;

    public Move(Player player) {
        this.player = player;
    }
}
