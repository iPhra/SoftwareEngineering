package it.polimi.se2018.model.objectives.privateobjectives;

import it.polimi.se2018.model.Color;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Square;

import java.util.Objects;
import java.util.stream.StreamSupport;

public class ShadesOfBlueObjective extends PrivateObjective {
    private static ShadesOfBlueObjective instance = null;
    private static final Color color = Color.BLUE;

    private ShadesOfBlueObjective(String title){
        super(title);
    }
    private static synchronized ShadesOfBlueObjective createInstance(String title){
        if (instance==null) instance = new ShadesOfBlueObjective(title);
        return instance;
    }

    public static ShadesOfBlueObjective instance(String title){
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