package it.polimi.se2018.model.objectives.publicobjectives;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Square;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This is the column color variety objective card. It extends {@link it.polimi.se2018.model.objectives.publicobjectives.PublicObjective}
 * Design pattern Singleton is used in this class, because only one instance is used in every running game
 * @author Emilio Imperiali
 */
public class ColumnColorVarietyObjective extends PublicObjective {
    private static ColumnColorVarietyObjective instance = null;

    private ColumnColorVarietyObjective(String title){
        super(title);
        description = "Columns with no repeated colors. 5 Point";
    }

    /**
     * This method creates the instance of this card, it's needed because of the Singleton pattern used here
     * @param title it's the title of this card
     * @return the instance of this card
     */
    private static synchronized ColumnColorVarietyObjective createInstance(String title){
        if (instance==null) instance = new ColumnColorVarietyObjective(title);
        return instance;
    }

    /**
     * @param title it's the title of this card
     * @return a new instance of this card if does not exist, the existing instance otherwise (as expected in the
     * Singleton pattern)
     */
    public static ColumnColorVarietyObjective instance(String title){
        if (instance==null) createInstance(title);
        return instance;
    }

    /**
     * This method returns a predicate, it contains a lambda function that checks if given column is the column of the
     * square
     * @param column it's the column that method must check if it's the column of the square
     * @return the result of the lambda function
     */
    private Predicate<Square> filterCol(final int column) {
        return square -> square.getCol() == column;
    }

    /**
     * The points are evaluated as it follows: this objective gives 5 points to the player for each column of his window
     * with no repeated colors
     * @param player the player whose points must be evaluated
     * @return the points given by this card to the player
     */
    @Override
    public int evalPoints(Player player) {
        return ( (int)Stream.of(0,1,2,3,4) //the 5 columns
                .map(column ->
                        StreamSupport.stream(player.getWindow().spliterator(), false)
                                .filter(filterCol(column))
                                .map(Square::getDie)
                                .filter(Objects::nonNull)
                                .map(Die::getColor)
                                .distinct()
                                .count()
                )
                .filter(distinctColors -> distinctColors == 4)
                .count() * 5);  //5 points for each completed column
    }
}
