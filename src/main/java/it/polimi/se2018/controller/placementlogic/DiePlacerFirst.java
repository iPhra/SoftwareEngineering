package it.polimi.se2018.controller.placementlogic;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Window;
import it.polimi.se2018.network.messages.Coordinate;

public class DiePlacerFirst extends DiePlacer{

    public DiePlacerFirst(Die die, Coordinate coordinate, Window window) {
        super(die,coordinate, window);
    }

    //row and col are indexes, they start from 0
    private boolean isOnEdge() {
        return (square.getRow()== window.getRows()-1 || square.getRow() == 0 || square.getCol()== window.getCols()-1 || square.getCol() == 0);
    }

    @Override
    public boolean checkCondition() {
        return square.isEmpty() && square.sameColor(die) && square.sameValue(die) && isOnEdge();
    }
}
