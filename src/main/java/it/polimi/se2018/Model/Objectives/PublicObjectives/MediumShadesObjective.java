package it.polimi.se2018.Model.Objectives.PublicObjectives;

import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Model.Square;

import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class MediumShadesObjective extends PublicObjective {
    private static MediumShadesObjective instance = null;

    private MediumShadesObjective(String imagePath, String title){
        super(imagePath,title);
    }
    private synchronized static MediumShadesObjective createInstance(String imagePath, String title){
        if (instance==null) instance = new MediumShadesObjective(imagePath, title);
        return instance;
    }

    public static MediumShadesObjective instance(String imagePath, String title){
        if (instance==null) createInstance(imagePath, title);
        return instance;
    }

    private static Predicate<Square> checkIfContainsValue(final int value) {
        return square -> square.getDie().getValue() == value;
    }

    public int evalPoints(Player player) {
        Stream<Square> SquareStream = StreamSupport.stream(player.getMap().spliterator(), false);
        return (Stream.of(3,4)
                .map(value ->
                        SquareStream
                                .anyMatch(checkIfContainsValue(value))
                )
                .anyMatch(isContained -> !isContained)) ? 0 : 2;
    }
}
