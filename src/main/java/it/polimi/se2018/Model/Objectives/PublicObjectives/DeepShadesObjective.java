package it.polimi.se2018.Model.Objectives.PublicObjectives;

import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Model.Square;

import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DeepShadesObjective extends PublicObjective {
    private static DeepShadesObjective instance = null;

    private DeepShadesObjective(String imagePath, String title) {
        super(imagePath, title);
    }

    private synchronized static DeepShadesObjective createInstance(String imagePath, String title) {
        if (instance == null) instance = new DeepShadesObjective(imagePath, title);
        return instance;
    }

    public static DeepShadesObjective instance(String imagePath, String title) {
        if (instance == null) createInstance(imagePath, title);
        return instance;
    }

    private static Predicate<Square> checkIfContainsValue(final int value) {
        return square -> square.getDie().getValue() == value;
    }

    public int evalPoints(Player player) {
        Stream<Square> SquareStream = StreamSupport.stream(player.getMap().spliterator(), false);
        return (Stream.of(5, 6)
                .map(value ->
                        SquareStream
                                .anyMatch(checkIfContainsValue(value))
                )
                .anyMatch(isContained -> !isContained)) ? 0 : 2;
    }
}
