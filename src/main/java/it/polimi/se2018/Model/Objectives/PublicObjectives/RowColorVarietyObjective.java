package it.polimi.se2018.Model.Objectives.PublicObjectives;

import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Model.Square;

import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RowColorVarietyObjective extends PublicObjective {
    private static RowColorVarietyObjective instance = null;

    private RowColorVarietyObjective(String imagePath, String title){
        super(imagePath,title);
    }
    private synchronized static RowColorVarietyObjective createInstance(String imagePath, String title){
        if (instance==null) instance = new RowColorVarietyObjective(imagePath, title);
        return instance;
    }

    public static RowColorVarietyObjective instance(String imagePath, String title){
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
                                .map(square -> square.getDie().getColor())
                                .distinct()
                                .count()
                )
                .anyMatch(distinctColors -> distinctColors == 5)) ? 0 : 6;  //because there are 5 cols
    }
}
