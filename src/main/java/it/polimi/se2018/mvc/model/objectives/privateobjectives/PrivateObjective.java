package it.polimi.se2018.mvc.model.objectives.privateobjectives;

import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.mvc.model.objectives.ObjectiveCard;

import java.util.Objects;
import java.util.stream.StreamSupport;

/**
 * This is the abstract private objective card. it extends {@link ObjectiveCard}
 */
public abstract class PrivateObjective extends ObjectiveCard {
    private final Color color;

    PrivateObjective(String title, Color color) {
        super(title);
        this.color = color;
    }

    /**
     * The points are evaluated as it follows: all the values of the colored dice in the player's window are added. The
     * result value represents the points given to the player by this objective
     * @param player the player whose points must be evaluated
     * @return the points given by this card to the player
     */
    public int evalPoints(Player player){
        return StreamSupport.stream(player.getWindow().spliterator(),false)
                .map(Square::getDie)
                .filter(Objects::nonNull)
                .filter(die -> die.getColor()==color)
                .map(Die::getValue)
                .mapToInt(value -> value)
                .sum();
    }

    @Override
    public String toString() {
        return "Private Objective:" + "\n" + "Title: \"" + getTitle() + "\"      Effect: \"" + getDescription() + "\"";
    }
}
