package it.polimi.se2018.model.objectives.publicobjectives;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Square;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class LightShadesObjective extends PublicObjective {
    private static LightShadesObjective instance = null;

    private LightShadesObjective(String title){
        super(title);
    }

    private static synchronized LightShadesObjective createInstance(String title){
        if (instance==null) instance = new LightShadesObjective(title);
        return instance;
    }

    public static LightShadesObjective instance(String title){
        if (instance==null) createInstance(title);
        return instance;
    }

    private Predicate<Square> checkIfContainsValue(final int value) {
        return square -> square.getDie() != null && square.getDie().getValue() == value;
    }

    @Override
    public int evalPoints(Player player) {
        Optional<Integer> min = (Stream.of(1,2)
                .map(value -> ( (int)StreamSupport.stream(player.getWindow().spliterator(), false)
                        .filter(checkIfContainsValue(value))
                        .count()))
                .reduce(Integer::min));
        return (min.map(integer -> integer * 2).orElse(0));

    }
}
