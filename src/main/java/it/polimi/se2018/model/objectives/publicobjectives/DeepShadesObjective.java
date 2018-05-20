package it.polimi.se2018.model.objectives.publicobjectives;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Square;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DeepShadesObjective extends PublicObjective {
    private static DeepShadesObjective instance = null;

    private DeepShadesObjective(String title) {
        super(title);
        description = "Sets of 5 & 6 values anywhere. 2 Point";
    }

    private static synchronized DeepShadesObjective createInstance(String title) {
        if (instance == null) instance = new DeepShadesObjective(title);
        return instance;
    }

    public static DeepShadesObjective instance(String title) {
        if (instance == null) createInstance(title);
        return instance;
    }

    private Predicate<Square> checkIfContainsValue(final int value) {
        return square -> square.getDie() != null && square.getDie().getValue() == value;
    }

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
