package it.polimi.se2018.mvc.controller.placementlogic;

import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.network.messages.Coordinate;

/**
 * This method is used to place the using all restriction.
 * This placer is used by the place of the turn.
 */
public class DiePlacerNormal extends DiePlacer {

    public DiePlacerNormal(Die die, Coordinate coordinate, Window window) {
        super(die,coordinate, window);
    }

    /**
     * Check if a die could go in a specific square or not
     * @return true if the die could go, false otherwise
     */
    @Override
    public boolean checkCondition() {
        return square.isEmpty() && square.sameColor(die) && square.sameValue(die) && isValueOk() && isColorOk()  && hasSurroundingDice();
    }
}
