package it.polimi.se2018.Model.PlacementLogic;

import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Model.Map;
import it.polimi.se2018.Network.Messages.Coordinate;

public class DiePlacerNoColor extends DiePlacer {

    public DiePlacerNoColor(Die die, Coordinate coordinate, Map map) {
        super(die,coordinate,map);
    }

    @Override
    public boolean checkCondition() {
        return square.isEmpty() && square.sameValue(die) && isValueOk() && hasSurroundingDice();
    }
}
