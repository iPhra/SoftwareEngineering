package it.polimi.se2018.mvc.controller.placementlogic;

import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.toolcards.CopperFoilBurnisher;
import it.polimi.se2018.network.messages.Coordinate;

/**
 * This placer don't check the condition of value.
 * The die could go in a square of a different value of the die.
 * The die could go in a square near die of the same value of the die to place.
 * This placer is used by {@link CopperFoilBurnisher}
 */
public class DiePlacerNoValue extends DiePlacer {

    public DiePlacerNoValue(Die die, Coordinate coordinate, Window window) {
        super(die,coordinate, window);
    }

    /**
     * Check if a die could go in a specific square or not
     * @return true if the die could go, false otherwise
     */
    @Override
    public boolean checkCondition() {
        return square.isEmpty() && square.sameColor(die) && isColorOk() && hasSurroundingDice();
    }
}
