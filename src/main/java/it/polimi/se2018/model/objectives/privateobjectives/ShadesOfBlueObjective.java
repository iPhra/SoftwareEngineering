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

    private ShadesOfBlueObjective(String imagePath, String title){
        super(imagePath,title);
    }

    private static synchronized ShadesOfBlueObjective createInstance(String imagePath, String title){
        if (instance==null) instance = new ShadesOfBlueObjective(imagePath, title);
        return instance;
    }

    public static ShadesOfBlueObjective instance(String imagePath, String title){
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