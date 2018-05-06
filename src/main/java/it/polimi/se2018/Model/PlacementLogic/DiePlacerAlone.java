package it.polimi.se2018.Model.PlacementLogic;

import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Model.Map;
import it.polimi.se2018.Model.Messages.Coordinate;

public class DiePlacerAlone extends DiePlacer {
    public DiePlacerAlone(Die die, Coordinate coordinate, Map map) {
        super(die,coordinate,map);
    }

    @Override
    public boolean checkCondition() {
        return square.isEmpty() && square.sameColor(die) && square.sameValue(die) && isValueOk() && isColorOk();
    }
}
