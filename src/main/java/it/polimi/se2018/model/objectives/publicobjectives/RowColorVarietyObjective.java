package it.polimi.se2018.model.objectives.publicobjectives;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Square;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RowColorVarietyObjective extends PublicObjective {
    private static RowColorVarietyObjective instance = null;

    private RowColorVarietyObjective(String title){
        super(title);
        description = "Rows with no repeated colors";
    }

    private static synchronized RowColorVarietyObjective createInstance(String title){
        if (instance==null) instance = new RowColorVarietyObjective(title);
        return instance;
    }

    public static RowColorVarietyObjective instance(String title){
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
                                .map(Die::getColor)
                                .distinct()
                                .count()
                )
                .filter(distinctColors -> distinctColors == 5)  //because there are 5 columns
                .count() * 6); //6 points for each row
    }
}
