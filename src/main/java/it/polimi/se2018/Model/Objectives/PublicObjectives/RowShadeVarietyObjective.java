package it.polimi.se2018.Model.Objectives.PublicObjectives;

import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Model.Square;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RowShadeVarietyObjective  extends PublicObjective{
    private static RowShadeVarietyObjective instance = null;

    private RowShadeVarietyObjective(String imagePath, String title){
        super(imagePath,title);
    }

    private static synchronized RowShadeVarietyObjective createInstance(String imagePath, String title){
        if (instance==null) instance = new RowShadeVarietyObjective(imagePath, title);
        return instance;
    }

    public static RowShadeVarietyObjective instance(String imagePath, String title){
        if (instance==null) createInstance(imagePath, title);
        return instance;
    }

    private Predicate<Square> filterRow(final int row) {
        return square -> square.getRow() == row;
    }

    @Override
    public int evalPoints(Player player) {
        return ( (int)Stream.of(1,2,3,4) //the 4 rows
                .map(row ->
                        StreamSupport.stream(player.getMap().spliterator(), false)
                                .filter(filterRow(row))
                                .map(Square::getDie)
                                .filter(Objects::nonNull)
                                .map(Die::getColor)
                                .distinct()
                                .count()
                )
                .filter(distinctValues -> distinctValues == 5) //because there are 5 columns
                .count() * 5);  //5 points for each row
    }
}
