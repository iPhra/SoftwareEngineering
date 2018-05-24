package it.polimi.se2018.mvc.model.objectives.privateobjectives;

import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;

import java.util.Objects;
import java.util.stream.StreamSupport;

/**
 * This is the shades of purple objective card. It extends {@link PrivateObjective}
 * Design pattern Singleton is used in this class, because only one instance is used in every running game
 * @author Emilio Imperiali
 */
public class ShadesOfPurpleObjective extends PrivateObjective {
    private static ShadesOfPurpleObjective instance = null;
    private static final Color color = Color.PURPLE;

    private ShadesOfPurpleObjective(String title){
        super(title);
        description = "Sum of values on purple dice";
    }

    /**
     * This method creates the instance of this card, it's needed because of the Singleton pattern used here
     * @param title it's the title of this card
     * @return the instance of this card
     */
    private static synchronized ShadesOfPurpleObjective createInstance(String title){
        if (instance==null) instance = new ShadesOfPurpleObjective(title);
        return instance;
    }

    /**
     * @param title it's the title of this card
     * @return a new instance of this card if does not exist, the existing instance otherwise (as expected in the
     * Singleton pattern)
     */
    public static ShadesOfPurpleObjective instance(String title){
        if (instance==null) createInstance(title);
        return instance;
    }

    /**
     * The points are evaluated as it follows: all the values of the purple dice in the player's window are added. The
     * result number represents the points given to the player by this objective
     * @param player the player whose points must be evaluated
     * @return the points given by this card to the player
     */
    @Override
    public int evalPoints(Player player){
        return StreamSupport.stream(player.getWindow().spliterator(),false)
                .map(Square::getDie)
                .filter(Objects::nonNull)
                .filter(die -> die.getColor()==color)
                .map(Die::getValue)
                .mapToInt(value -> value)
                .sum();
    }
}
