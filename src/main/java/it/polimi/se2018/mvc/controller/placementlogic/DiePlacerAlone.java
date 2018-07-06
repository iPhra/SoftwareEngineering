package it.polimi.se2018.mvc.controller.placementlogic;

import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.toolcards.CorkBackedStraightedge;
import it.polimi.se2018.network.messages.Coordinate;

/**
 * This placer don't check the condition that the die has surronding dice
 * This placer is used by {@link CorkBackedStraightedge}
 */
public class DiePlacerAlone extends DiePlacer {

    public DiePlacerAlone(Die die, Coordinate coordinate, Window window) {
        super(die,coordinate, window);
    }

    /**
     * Check if a die could go in a specific square or not
     * @return true if the die could go, false otherwise
     */
    @Override
    public boolean checkCondition() {
        return square.isEmpty() && square.sameColor(die) && square.sameValue(die);
    }
}
