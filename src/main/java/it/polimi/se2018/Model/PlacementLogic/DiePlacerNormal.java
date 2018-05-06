package it.polimi.se2018.Model.PlacementLogic;

import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Model.Map;
import it.polimi.se2018.Model.Messages.Coordinate;

public class DiePlacerNormal extends DiePlacer {
    public DiePlacerNormal(Die die, Coordinate coordinate, Map map) {
        super(die,coordinate,map);
        this.die = die;
        this.coordinate = coordinate;
        this.map = map;
        this.square = map.getSquare(coordinate.getRow(),coordinate.getCol());
    }

    @Override
    public boolean checkCondition() {
        return square.isEmpty() && square.sameColor(die) && square.sameValue(die) && isValueOk() && isColorOk(); // && hasSurroundingDice(row, col);
    }
}
