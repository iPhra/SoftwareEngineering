package it.polimi.se2018.Model.Objectives.PublicObjectives;

import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Model.Square;

import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RowShadeVarietyObjective  extends PublicObjective{
    private static RowShadeVarietyObjective instance = null;

    private RowShadeVarietyObjective(String imagePath, String title){
        super(imagePath,title);
    }
    private synchronized static RowShadeVarietyObjective createInstance(String imagePath, String title){
        if (instance==null) instance = new RowShadeVarietyObjective(imagePath, title);
        return instance;
    }

    public static RowShadeVarietyObjective instance(String imagePath, String title){
        if (instance==null) createInstance(imagePath, title);
        return instance;
    }

    private static Predicate<Square> filterCol(final int row) {
        return square -> square.getRow() == row;
    }

    public int evalPoints(Player player) {
        Stream<Square> SquareStream = StreamSupport.stream(player.getMap().spliterator(), false);
        return (Stream.of(1,2,3,4) //the 4 rows
                .map(row ->
                        SquareStream
                                .filter(filterCol(row))
                                .map(square -> square.getDie().getValue())
                                .distinct()
                                .count()
                )
                .anyMatch(distinctValues -> distinctValues == 5)) ? 0 : 5;  //because there are 5 cols
    }
}
