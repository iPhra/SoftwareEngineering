package it.polimi.se2018.network.messages.requests;

import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.toolcards.FluxRemover;
import it.polimi.se2018.network.messages.Coordinate;


import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a message from a player wishing to specify the inputs needed to use the Tool Card in his hand
 */
public class ToolCardMessage extends Message {

    /**
     * This represents a condition from Tool Cards that let the player choose between two options
     * Used by {@link FluxRemover} and {@link it.polimi.se2018.mvc.model.toolcards.GrozingPliers}
     */
    private boolean condition;

    /**
     * This is the value of the Tool Card as displayed on the array in {@link Board}
     */
    private final int toolCardNumber;

    /**
     * This contains all the positions you want to move a die from, each value in the Array List is associated to a die to move
     */
    private final List<Coordinate> startingPosition;

    /**
     * This contains all the positions you want to move a die to, each value in the Array List is associated to a die to move
     */
    private final List<Coordinate> finalPosition;

    /**
     * This contains all the positions on the Draft Pool of a Die you want to select, each value is associated to a die to select
     */
    private int draftPoolPosition;

    /**
     * This contains all the positions on the Round Tracker of a Die you want to select, each value is associated to a die to select
     */
    private Coordinate roundTrackerPosition; //x is the Turn, y is the position

    public ToolCardMessage(int playerID, int stateID, int toolCardNumber) {
        super(playerID, stateID);
        startingPosition = new ArrayList<>();
        this.toolCardNumber=toolCardNumber;
        finalPosition = new ArrayList<>();
    }

    /**
     * @return the starting positions of dice you want to move
     */
    public List<Coordinate> getStartingPosition() {
        return startingPosition;
    }

    /**
     * @return the final positions of dice you want to move
     */
    public List<Coordinate> getFinalPosition() {
        return finalPosition;
    }

    /**
     * @return the position on the Draft Pool of the dice you want to select
     */
    public int getDraftPoolPosition() {
        return draftPoolPosition;
    }

    /**
     * @return the position on the Round Trucker of the dice you want to select
     */
    public Coordinate getRoundTrackerPosition() {
        return roundTrackerPosition;
    }

    /**
     * @param coordinate is the coordinate of the starting position you want to add to the list
     */
    public void addStartingPosition(Coordinate coordinate) {
        startingPosition.add(coordinate);
    }

    /**
     * @param coordinate is the coordinate of the final position you want to add to the list
     */
    public void addFinalPosition(Coordinate coordinate) {
        finalPosition.add(coordinate);
    }

    /**
     * @param coordinate is the coordinate of the die on the Draft Pool you want to add to the list
     */
    public void addDraftPoolPosition(int coordinate) {
        draftPoolPosition=coordinate;
    }

    /**
     * @param coordinate is the coordinate of the die on the Round Tracker you want to add to the list
     */
    public void addRoundTrackerPosition(Coordinate coordinate) {
        roundTrackerPosition= coordinate;
    }

    /**
     * @return the value of the Tool Card
     */
    public int getToolCardNumber() {
        return toolCardNumber;
    }

    public boolean isCondition() {
        return condition;
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    @Override
    public void handle(MessageHandler handler) {
        handler.handleMove(this);
    }
}
