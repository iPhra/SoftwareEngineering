package it.polimi.se2018.mvc.model.objectives.publicobjectives;

import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This is the column shade variety objective card. It extends {@link PublicObjective}
 * Design pattern Singleton is used in this class, because only one instance is used in every running game
 */
public class ColumnShadeVarietyObjective extends PublicObjective{
    private static ColumnShadeVarietyObjective instance = null;

    private ColumnShadeVarietyObjective(String title, String imagePath){
        super(title);
        description = "4 points for each column with no repeated value";
        this.imagePath = imagePath;
    }

    /**
     * @param title it's the title of this card
     * @return a new instance of this card if does not exist, the existing instance otherwise (as expected in the
     * Singleton pattern)
     */
    public static ColumnShadeVarietyObjective instance(String title, String imagePath){
        if (instance==null) instance = new ColumnShadeVarietyObjective(title,imagePath);
        return instance;
    }

    /**
     * This method returns a predicate, it contains a lambda function that checks if given column is the column of the
     * square
     * @param column it's the column that method must check if it's the column of the square
     * @return the result of the lambda function
     */
    private static Predicate<Square> filterCol(final int column) {
        return square -> square.getCol() == column;
    }

    /**
     * The points are evaluated as it follows: this objective gives 4 points to the player for each column of his window
     * with no repeated values
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
                                .map(Die::getValue)
                                .distinct()
                                .count()
                )
                .filter(distinctValues -> distinctValues == 4)
                .count() * 4);
    }
}
