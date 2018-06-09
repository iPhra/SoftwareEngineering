package it.polimi.se2018.network.messages.requests;

import it.polimi.se2018.network.messages.Coordinate;

/**
 * This class represents a message from a player wishing to place his drafted die on his scheme
 */
public class PlaceMessage extends Message {
    /**
     * This is the coordinate of the position where the player wants to place his die
     */
    private Coordinate finalPosition;

    public PlaceMessage(int playerID, int stateID, Coordinate finalPosition) {
        super(playerID,stateID);
        this.finalPosition = finalPosition;
    }

    /**
     * @return the position where the player wants to place his die
     */
    public Coordinate getFinalPosition() {
        return finalPosition;
    }

    /**
     * Sets the position where the placer wants to place his die
     * @param finalPosition is the coordinate representing the position
     */
    public void setFinalPosition(Coordinate finalPosition) {
        this.finalPosition = finalPosition;
    }

    /**
     * Uses the handler to handle this specific place request
     * @param handler is the object who will handle this request
     */
    @Override
    public void handle(MessageHandler handler) {
        handler.handleMove(this);
    }
}
