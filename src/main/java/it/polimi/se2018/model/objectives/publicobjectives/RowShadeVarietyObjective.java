package it.polimi.se2018.model.objectives.publicobjectives;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Square;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This is the row shade variety objective card. It extends {@link it.polimi.se2018.model.objectives.publicobjectives.PublicObjective}
 * Design pattern Singleton is used in this class, because only one instance is used in every running game
 * @author Emilio Imperiali
 */
public class RowShadeVarietyObjective  extends PublicObjective{
    private static RowShadeVarietyObjective instance = null;

    private RowShadeVarietyObjective(String title){
        super(title);
        description = "Rows with no repeated values";
    }

    /**
     * This method creates the instance of this card, it's needed because of the Singleton pattern used here
     * @param title it's the title of this card
     * @return the instance of this card
     */
    private static synchronized RowShadeVarietyObjective createInstance(String title){
        if (instance==null) instance = new RowShadeVarietyObjective(title);
        return instance;
    }

    /**
     * @param title it's the title of this card
     * @return a new instance of this card if does not exist, the existing instance otherwise (as expected in the
     * Singleton pattern)
     */
    public static RowShadeVarietyObjective instance(String title){
        if (instance==null) createInstance(title);
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
     * The points are evaluated as it follows: this objective gives 5 points to the player for each row of his window
     * with no repeated values
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
                                .map(Die::getValue)
                                .distinct()
                                .count()
                )
                .filter(distinctValues -> distinctValues == 5) //because there are 5 columns
                .count() * 5);  //5 points for each row
    }
}
