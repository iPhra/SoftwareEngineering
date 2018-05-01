package it.polimi.se2018.Model.Objectives.PublicObjectives;

import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Model.Square;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ShadeVarietyObjective extends PublicObjective {
    private static ShadeVarietyObjective instance = null;

    private ShadeVarietyObjective(String imagePath, String title){
        super(imagePath,title);
    }

    private static synchronized ShadeVarietyObjective createInstance(String imagePath, String title){
        if (instance==null) instance = new ShadeVarietyObjective(imagePath, title);
        return instance;
    }

    public static ShadeVarietyObjective instance(String imagePath, String title){
        if (instance==null) createInstance(imagePath, title);
        return instance;
    }

    private Predicate<Square> checkIfContainsValue(final int value) {
        return square -> square.getDie() != null && square.getDie().getValue() == value;
    }

    @Override
    public int evalPoints(Player player) {
        Optional<Integer> min = (Stream.of(1,2,3,4,5,6)
                .map(value -> ( (int)StreamSupport.stream(player.getMap().spliterator(), false)
                        .filter(checkIfContainsValue(value))
                        .count()))
                .reduce(Integer::min));
        return (min.map(integer -> integer * 5).orElse(0));

    }
}
