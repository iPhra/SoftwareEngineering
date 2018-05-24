package it.polimi.se2018.mvc.model.objectives.publicobjectives;

import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * This is the deep shades objective card. It extends {@link PublicObjective}
 * Design pattern Singleton is used in this class, because only one instance is used in every running game
 * @author Emilio Imperiali
 */
public class DeepShadesObjective extends PublicObjective {
    private static DeepShadesObjective instance = null;

    private DeepShadesObjective(String title) {
        super(title);
        description = "Sets of 5 & 6 values anywhere. #2 Points";
    }

    /**
     * This method creates the instance of this card, it's needed because of the Singleton pattern used here
     * @param title it's the title of this card
     * @return the instance of this card
     */
    private static synchronized DeepShadesObjective createInstance(String title) {
        if (instance == null) instance = new DeepShadesObjective(title);
        return instance;
    }

    /**
     * @param title it's the title of this card
     * @return a new instance of this card if does not exist, the existing instance otherwise (as expected in the
     * Singleton pattern)
     */
    public static DeepShadesObjective instance(String title) {
        if (instance == null) createInstance(title);
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
     * The points are evaluated as it follows: this objective gives 2 points to the player for each set of 5 and 6
     * values anywhere
     * @param player the player whose points must be evaluated
     * @return the points given by this card to the player
     */
    @Override
    public int evalPoints(Player player) {
        Optional<Integer> min = (Stream.of(5,6)
                .map(value -> ( (int)StreamSupport.stream(player.getWindow().spliterator(), false)
                        .filter(checkIfContainsValue(value))
                        .count()))
                .reduce(Integer::min));
        return (min.map(integer -> integer * 2).orElse(0));

    }
}