package it.polimi.se2018.Model.PlacementLogic;

import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Model.Map;
import it.polimi.se2018.Network.Messages.Coordinate;

public class DiePlacerFirst extends DiePlacer{

    public DiePlacerFirst(Die die, Coordinate coordinate, Map map) {
        super(die,coordinate,map);
    }

    //row and col are indexes, they start from 0
    private boolean isOnEdge() {
        return (square.getRow()==map.getRows()-1 || square.getRow() == 0 || square.getCol()==map.getCols()-1 || square.getCol() == 0);
    }

    @Override
    public boolean checkCondition() {
        return square.isEmpty() && square.sameColor(die) && square.sameValue(die) && isOnEdge();
    }
}
