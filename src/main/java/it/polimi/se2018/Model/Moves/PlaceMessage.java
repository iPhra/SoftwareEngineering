package it.polimi.se2018.Model.Moves;

import it.polimi.se2018.Model.Player;

public class PlaceMessage {
    private final Player player;
    private Coordinate finalPosition;

    public PlaceMessage(Coordinate finalPosition, Player player) {
        this.finalPosition = finalPosition;
        this.player = player;
    }

    public Coordinate getFinalPosition() {
        return finalPosition;
    }

    public void setFinalPosition(Coordinate finalPosition) {
        this.finalPosition = finalPosition;
    }

    public Player getPlayer() {
        return player;
    }
}
