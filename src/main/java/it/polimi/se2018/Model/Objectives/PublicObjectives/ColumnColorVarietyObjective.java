package it.polimi.se2018.Model.Objectives.PublicObjectives;

import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Model.Square;

import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ColumnColorVarietyObjective extends PublicObjective {
    private static ColumnColorVarietyObjective instance = null;

    private ColumnColorVarietyObjective(String imagePath, String title){
        super(imagePath,title);
    }
    private synchronized static ColumnColorVarietyObjective createInstance(String imagePath, String title){
        if (instance==null) instance = new ColumnColorVarietyObjective(imagePath, title);
        return instance;
    }

    public static ColumnColorVarietyObjective instance(String imagePath, String title){
        if (instance==null) createInstance(imagePath, title);
        return instance;
    }

    private static Predicate<Square> filterCol(final int column) {
        return square -> square.getCol() == column;
    }

    public int evalPoints(Player player) {
        Stream<Square> SquareStream = StreamSupport.stream(player.getMap().spliterator(), false);
        return (Stream.of(1,2,3,4,5) //the 5 columns
                .map(column ->
                        SquareStream
                                .filter(filterCol(column))
                                .map(square -> square.getDie().getColor())
                                .distinct()
                                .count()
                )
                .anyMatch(distinctColors -> distinctColors == 4)) ? 0 : 5;  //because there are 4 rows
    }
}
