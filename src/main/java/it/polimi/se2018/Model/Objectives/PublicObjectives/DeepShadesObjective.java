package it.polimi.se2018.Model.Objectives.PublicObjectives;

import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Model.Square;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DeepShadesObjective extends PublicObjective {
    private static DeepShadesObjective instance = null;

    private DeepShadesObjective(String imagePath, String title) {
        super(imagePath, title);
    }

    private static synchronized DeepShadesObjective createInstance(String imagePath, String title) {
        if (instance == null) instance = new DeepShadesObjective(imagePath, title);
        return instance;
    }

    public static DeepShadesObjective instance(String imagePath, String title) {
        if (instance == null) createInstance(imagePath, title);
        return instance;
    }

    private static Predicate<Square> checkIfContainsValue(final int value) {
        return square -> square.getDie() != null && square.getDie().getValue() == value;
    }

    @Override
    public int evalPoints(Player player) {
        Optional<Integer> min = (Stream.of(5,6)
                .map(value -> ( (int)StreamSupport.stream(player.getMap().spliterator(), false)
                        .filter(checkIfContainsValue(value))
                        .count()))
                .reduce(Integer::min));
        return (min.map(integer -> integer * 2).orElse(0));

    }
}
