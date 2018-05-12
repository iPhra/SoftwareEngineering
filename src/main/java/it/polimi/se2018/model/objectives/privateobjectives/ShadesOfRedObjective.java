package it.polimi.se2018.model.objectives.privateobjectives;

import it.polimi.se2018.model.Color;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Square;

import java.util.Objects;
import java.util.stream.StreamSupport;

public class ShadesOfRedObjective extends PrivateObjective {
    private static ShadesOfRedObjective instance = null;
    private static final Color color = Color.RED;

    private ShadesOfRedObjective(String imagePath, String title){
        super(imagePath,title);
    }
    private static synchronized ShadesOfRedObjective createInstance(String imagePath, String title){
        if (instance==null) instance = new ShadesOfRedObjective(imagePath, title);
        return instance;
    }

    public static ShadesOfRedObjective instance(String imagePath, String title){
        if (instance==null) createInstance(imagePath, title);
        return instance;
    }

    @Override
    public int evalPoints(Player player){
        return (int) StreamSupport.stream(player.getMap().spliterator(),false)
                .map(Square::getDie)
                .filter(Objects::nonNull)
                .filter(die -> die.getColor()==color)
                .map(Die::getValue)
                .mapToInt(value -> value)
                .sum();
    }
}
