package it.polimi.se2018.Model.Objectives.PublicObjectives;

import it.polimi.se2018.Model.Color;
import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Model.Square;

import java.util.EnumSet;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ColorVarietyObjective extends PublicObjective {
    private static ColorVarietyObjective instance = null;

    private ColorVarietyObjective(String imagePath, String title) {
        super(imagePath, title);
    }

    private synchronized static ColorVarietyObjective createInstance(String imagePath, String title) {
        if (instance == null) instance = new ColorVarietyObjective(imagePath, title);
        return instance;
    }

    public static ColorVarietyObjective instance(String imagePath, String title) {
        if (instance == null) createInstance(imagePath, title);
        return instance;
    }


    private static Predicate<Square> checkIfContainsColor(final Color color) {
        return square -> square.getDie().getColor() == color;
    }


    public int evalPoints(Player player) {
        Stream<Square> SquareStream = StreamSupport.stream(player.getMap().spliterator(), false);
        return (Stream.of(Color.values())
                .map(color ->
                        SquareStream
                                .anyMatch(checkIfContainsColor(color))
                )
                .anyMatch(isContained -> !isContained)) ? 0 : 4;
    }

}

