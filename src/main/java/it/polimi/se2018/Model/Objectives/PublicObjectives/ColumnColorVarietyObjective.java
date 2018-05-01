package it.polimi.se2018.Model.Objectives.PublicObjectives;

import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Model.Square;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ColumnColorVarietyObjective extends PublicObjective {
    private static ColumnColorVarietyObjective instance = null;

    private ColumnColorVarietyObjective(String imagePath, String title){
        super(imagePath,title);
    }

    private static synchronized ColumnColorVarietyObjective createInstance(String imagePath, String title){
        if (instance==null) instance = new ColumnColorVarietyObjective(imagePath, title);
        return instance;
    }

    public static ColumnColorVarietyObjective instance(String imagePath, String title){
        if (instance==null) createInstance(imagePath, title);
        return instance;
    }

    private Predicate<Square> filterCol(final int column) {
        return square -> square.getCol() == column;
    }

    @Override
    public int evalPoints(Player player) {
        return ( (int)Stream.of(1,2,3,4,5) //the 5 columns
                .map(column ->
                        StreamSupport.stream(player.getMap().spliterator(), false)
                                .filter(filterCol(column))
                                .map(Square::getDie)
                                .filter(Objects::nonNull)
                                .map(Die::getColor)
                                .distinct()
                                .count()
                )
                .filter(distinctColors -> distinctColors == 4)
                .count() * 5);  //5 points for each completed column
    }
}
