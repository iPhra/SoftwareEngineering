package it.polimi.se2018.model.objectives.publicobjectives;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Square;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class MediumShadesObjective extends PublicObjective {
    private static MediumShadesObjective instance = null;

    private MediumShadesObjective(String imagePath, String title){
        super(imagePath,title);
    }

    private static synchronized MediumShadesObjective createInstance(String imagePath, String title){
        if (instance==null) instance = new MediumShadesObjective(imagePath, title);
        return instance;
    }

    public static MediumShadesObjective instance(String imagePath, String title){
        if (instance==null) createInstance(imagePath, title);
        return instance;
    }

    private Predicate<Square> checkIfContainsValue(final int value) {
        return square -> square.getDie() != null && square.getDie().getValue() == value;
    }

    @Override
    public int evalPoints(Player player) {
        Optional<Integer> min = (Stream.of(3,4)
                .map(value -> ( (int)StreamSupport.stream(player.getMap().spliterator(), false)
                        .filter(checkIfContainsValue(value))
                        .count()))
                .reduce(Integer::min));
        return (min.map(integer -> integer * 2).orElse(0));

    }
}
