package it.polimi.se2018.model.objectives.privateobjectives;

import it.polimi.se2018.model.Color;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Square;

import java.util.Objects;
import java.util.stream.StreamSupport;

public class ShadesOfPurpleObjective extends PrivateObjective {
    private static ShadesOfPurpleObjective instance = null;
    private static final Color color = Color.PURPLE;

    private ShadesOfPurpleObjective(String title){
        super(title);
    }
    private static synchronized ShadesOfPurpleObjective createInstance(String title){
        if (instance==null) instance = new ShadesOfPurpleObjective(title);
        return instance;
    }

    public static ShadesOfPurpleObjective instance(String title){
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
