package it.polimi.se2018.model.objectives.publicobjectives;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Square;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RowShadeVarietyObjective  extends PublicObjective{
    private static RowShadeVarietyObjective instance = null;

    private RowShadeVarietyObjective(String title){
        super(title);
    }

    private static synchronized RowShadeVarietyObjective createInstance(String title){
        if (instance==null) instance = new RowShadeVarietyObjective(title);
        return instance;
    }

    public static RowShadeVarietyObjective instance(String title){
        if (instance==null) createInstance(title);
        return instance;
    }

    private Predicate<Square> filterRow(final int row) {
        return square -> square.getRow() == row;
    }

    @Override
    public int evalPoints(Player player) {
        return ( (int)Stream.of(0,1,2,3) //the 4 rows
                .map(row ->
                        StreamSupport.stream(player.getWindow().spliterator(), false)
                                .filter(filterRow(row))
                                .map(Square::getDie)
                                .filter(Objects::nonNull)
                                .map(Die::getValue)
                                .distinct()
                                .count()
                )
                .filter(distinctValues -> distinctValues == 5) //because there are 5 columns
                .count() * 5);  //5 points for each row
    }
}
