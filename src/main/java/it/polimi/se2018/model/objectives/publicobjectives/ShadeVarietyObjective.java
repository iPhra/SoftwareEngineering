package it.polimi.se2018.model.objectives.publicobjectives;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Square;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ShadeVarietyObjective extends PublicObjective {
    private static ShadeVarietyObjective instance = null;

    private ShadeVarietyObjective(String title){
        super(title);
        description = "Sets of one of each values anywhere. 5 Point";
    }

    private static synchronized ShadeVarietyObjective createInstance(String title){
        if (instance==null) instance = new ShadeVarietyObjective(title);
        return instance;
    }

    public static ShadeVarietyObjective instance(String title){
        if (instance==null) createInstance(title);
        return instance;
    }

    private Predicate<Square> checkIfContainsValue(final int value) {
        return square -> square.getDie() != null && square.getDie().getValue() == value;
    }

    @Override
    public int evalPoints(Player player) {
        Optional<Integer> min = (Stream.of(1,2,3,4,5,6)
                .map(value -> ( (int)StreamSupport.stream(player.getWindow().spliterator(), false)
                        .filter(checkIfContainsValue(value))
                        .count()))
                .reduce(Integer::min));
        return (min.map(integer -> integer * 5).orElse(0));

    }
}
