package it.polimi.se2018.mvc.model.objectives.publicobjectives;

import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This is the shade variety objective card. It extends {@link PublicObjective}
 * Design pattern Singleton is used in this class, because only one instance is used in every running game
 */
public class ShadeVarietyObjective extends PublicObjective {
    private static ShadeVarietyObjective instance = null;

    private ShadeVarietyObjective(String imagePath){
        super("Shade Variety","5 points for each set of one of each value anywhere",imagePath);
    }

    /**
     * @return a new instance of this card if does not exist, the existing instance otherwise (as expected in the
     * Singleton pattern)
     */
    public static ShadeVarietyObjective instance(String imagePath){
        if (instance==null) instance = new ShadeVarietyObjective(imagePath);
        return instance;
    }

    /**
     * This method returns a predicate, it contains a lambda function that checks if given value is the value of the die
     * of a square
     * @param value it's the value that method must check if it's present in the die of the square
     * @return the result of the lambda function
     */
    private Predicate<Square> checkIfContainsValue(final int value) {
        return square -> square.getDie() != null && square.getDie().getValue() == value;
    }

    /**
     * The points are evaluated as it follows: this objective gives 5 points to the player for each set of one of each
     * value anywhere on his window
     * @param player the player whose points must be evaluated
     * @return the points given by this card to the player
     */
    @Override
    public int evalPoints(Player player) {
        Optional<Integer> min = (Stream.of(1,2,3,4,5,6)
                .map(value -> ( (int)StreamSupport.stream(player.getWindow().spliterator(), false)
                        .filter(checkIfContainsValue(value))
                        .count()))
                .reduce(Integer::min));
        return (min.map(integer -> integer * 5).orElse(0));

    }
}
