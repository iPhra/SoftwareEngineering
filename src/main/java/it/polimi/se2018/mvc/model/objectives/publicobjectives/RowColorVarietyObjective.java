package it.polimi.se2018.mvc.model.objectives.publicobjectives;

import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This is the row color variety objective card. It extends {@link PublicObjective}
 * Design pattern Singleton is used in this class, because only one instance is used in every running game
 */
public class RowColorVarietyObjective extends PublicObjective {
    private static RowColorVarietyObjective instance = null;

    private RowColorVarietyObjective(String imagePath){
        super("Row Color Variety", "6 points for each row with no repeated color", imagePath);
    }

    /**
     * @return a new instance of this card if does not exist, the existing instance otherwise (as expected in the
     * Singleton pattern)
     */
    public static RowColorVarietyObjective instance(){
        if (instance==null) instance = new RowColorVarietyObjective("/objectives/public_objectives/row_color_variety.png");
        return instance;
    }

    /**
     * This method returns a predicate, it contains a lambda function that checks if given row is the row of the square
     * @param row it's the row that method must check if it's the row of the square
     * @return the result of the lambda function
     */
    private Predicate<Square> filterRow(final int row) {
        return square -> square.getRow() == row;
    }

    /**
     * The points are evaluated as it follows: this objective gives 6 points to the player for each row of his window
     * with no repeated colors
     * @param player the player whose points must be evaluated
     * @return the points given by this card to the player
     */
    @Override
    public int evalPoints(Player player) {
        return ( (int)Stream.of(0,1,2,3) //the 4 rows
                .map(row ->
                        StreamSupport.stream(player.getWindow().spliterator(), false)
                                .filter(filterRow(row))
                                .map(Square::getDie)
                                .filter(Objects::nonNull)
                                .map(Die::getColor)
                                .distinct()
                                .count()
                )
                .filter(distinctColors -> distinctColors == 5)  //because there are 5 columns
                .count() * 6); //6 points for each row
    }
}
