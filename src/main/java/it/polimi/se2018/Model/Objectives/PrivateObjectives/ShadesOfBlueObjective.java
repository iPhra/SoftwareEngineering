package it.polimi.se2018.Model.Objectives.PrivateObjectives;

import it.polimi.se2018.Model.Color;
import it.polimi.se2018.Model.Player;

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
                .filter(square -> square.getDie().getColor()==color)
                .count();
    }
}