package it.polimi.se2018.mvc.controller.placementlogic;

import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.network.messages.Coordinate;

public class DiePlacerNoValue extends DiePlacer {

    public DiePlacerNoValue(Die die, Coordinate coordinate, Window window) {
        super(die,coordinate, window);
    }

    @Override
    public boolean checkCondition() {
        return square.isEmpty() && square.sameColor(die) && isColorOk() && hasSurroundingDice();
    }
}
