package it.polimi.se2018.Model.Messages;

import it.polimi.se2018.Model.Player;

public class PlaceMessage extends Message {
    private Coordinate finalPosition;

    public PlaceMessage(Coordinate finalPosition, Player player) {
        super(player);
        this.finalPosition = finalPosition;
    }

    public Coordinate getFinalPosition() {
        return finalPosition;
    }

    public void setFinalPosition(Coordinate finalPosition) {
        this.finalPosition = finalPosition;
    }

    @Override
    public void handle(MessageHandler handler) {
        handler.performMove(this);
    }

}
