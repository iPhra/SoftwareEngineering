package it.polimi.se2018.mvc.controller.placementlogic;

import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.network.messages.Coordinate;

/**
 * This placer use the rule of the first placement.
 * The die has to go the border and don't have to be near another die
 */
public class DiePlacerFirst extends DiePlacer{

    public DiePlacerFirst(Die die, Coordinate coordinate, Window window) {
        super(die,coordinate, window);
    }

    //row and col are indexes, they start from 0

    /**
     * This method check if the square where you would place the die is on the border of the window
     * @return true if it is on the border, false otherwise
     */
    private boolean isOnEdge() {
        return (square.getRow()== window.getRows()-1 || square.getRow() == 0 || square.getCol()== window.getCols()-1 || square.getCol() == 0);
    }

    /**
     * Check if a die could go in a specific square or not
     * @return true if the die could go, false otherwise
     */
    @Override
    public boolean checkCondition() {
        return square.isEmpty() && square.sameColor(die) && square.sameValue(die) && isOnEdge();
    }
}
