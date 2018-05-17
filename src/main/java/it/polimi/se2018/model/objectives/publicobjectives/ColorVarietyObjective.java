package it.polimi.se2018.model.objectives.publicobjectives;

import it.polimi.se2018.model.Color;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Square;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ColorVarietyObjective extends PublicObjective {
    private static ColorVarietyObjective instance = null;

    private ColorVarietyObjective(String title) {
        super(title);
    }

    private static synchronized ColorVarietyObjective createInstance(String title) {
        if (instance == null) instance = new ColorVarietyObjective(title);
        return instance;
    }

    public static ColorVarietyObjective instance(String title) {
        if (instance == null) createInstance(title);
        return instance;
    }


    private Predicate<Square> checkIfContainsColor(final Color color) {
        return (square -> square.getDie() != null && square.getDie().getColor() == color);
    }


    @Override
    public int evalPoints(Player player) {
        Optional<Integer> min = (Stream.of(Color.BLUE,Color.GREEN,Color.RED,Color.PURPLE,Color.YELLOW)
                .map(color -> ( (int)StreamSupport.stream(player.getWindow().spliterator(), false)
                        .filter(checkIfContainsColor(color))
                        .count()))
                .reduce(Integer::min));
        return (min.map(integer -> integer * 4).orElse(0));

    }

}

