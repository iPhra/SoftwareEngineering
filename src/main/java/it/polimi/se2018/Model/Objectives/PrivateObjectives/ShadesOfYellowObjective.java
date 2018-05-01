package it.polimi.se2018.Model.Objectives.PrivateObjectives;

import it.polimi.se2018.Model.Color;
import it.polimi.se2018.Model.Player;

import java.util.stream.StreamSupport;

public class ShadesOfYellowObjective extends PrivateObjective {
    private static ShadesOfYellowObjective instance = null;
    private static final Color color = Color.YELLOW;

    private ShadesOfYellowObjective(String imagePath, String title){
        super(imagePath,title);
    }
    private static synchronized ShadesOfYellowObjective createInstance(String imagePath, String title){
        if (instance==null) instance = new ShadesOfYellowObjective(imagePath, title);
        return instance;
    }

    public static ShadesOfYellowObjective instance(String imagePath, String title){
        if (instance==null) createInstance(imagePath, title);
        return instance;
    }

    @Override
    public int evalPoints(Player player){
        return (int) StreamSupport.stream(player.getMap().spliterator(),false)
                .filter(square -> square.getDie().getColor()==color)
                .count();
    }
}