package it.polimi.se2018.Model.PlacementLogic;

import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Model.Map;
import it.polimi.se2018.Network.Messages.Coordinate;

public class DiePlacerNoValue extends DiePlacer {

    public DiePlacerNoValue(Die die, Coordinate coordinate, Map map) {
        super(die,coordinate,map);
    }

    @Override
    public boolean checkCondition() {
        return square.isEmpty() && square.sameColor(die) && isColorOk() && hasSurroundingDice();
    }
}
