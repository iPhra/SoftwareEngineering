package it.polimi.se2018.model.objectives.privateobjectives;

import it.polimi.se2018.model.Color;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Square;

import java.util.Objects;
import java.util.stream.StreamSupport;

public class ShadesOfYellowObjective extends PrivateObjective {
    private static ShadesOfYellowObjective instance = null;
    private static final Color color = Color.YELLOW;

    private ShadesOfYellowObjective(String title){
        super(title);
        description = "Sum of values on yellow dice";
    }
    private static synchronized ShadesOfYellowObjective createInstance(String title){
        if (instance==null) instance = new ShadesOfYellowObjective(title);
        return instance;
    }

    public static ShadesOfYellowObjective instance(String title){
        if (instance==null) createInstance(title);
        return instance;
    }

    @Override
    public int evalPoints(Player player){
        return StreamSupport.stream(player.getWindow().spliterator(),false)
                .map(Square::getDie)
                .filter(Objects::nonNull)
                .filter(die -> die.getColor()==color)
                .map(Die::getValue)
                .mapToInt(value -> value)
                .sum();
    }
}