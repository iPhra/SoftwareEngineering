package it.polimi.se2018.Model.Objectives.PublicObjectives;

import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Model.Square;

import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ColumnShadeVarietyObjective extends PublicObjective{
    private static ColumnShadeVarietyObjective instance = null;

    private ColumnShadeVarietyObjective(String imagePath, String title){
        super(imagePath,title);
    }
    private synchronized static ColumnShadeVarietyObjective createInstance(String imagePath, String title){
        if (instance==null) instance = new ColumnShadeVarietyObjective(imagePath, title);
        return instance;
    }

    public static ColumnShadeVarietyObjective instance(String imagePath, String title){
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
                                .map(square -> square.getDie().getValue())
                                .distinct()
                                .count()
                )
                .anyMatch(distinctValues -> distinctValues == 4)) ? 0 : 4;  //because there are 4 rows
    }
}
