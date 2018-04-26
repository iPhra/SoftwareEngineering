package it.polimi.se2018.Model.Objectives.PrivateObjectives;

import it.polimi.se2018.Model.Color;
import it.polimi.se2018.Model.Player;

import java.util.stream.StreamSupport;

public class ShadesOfPurpleObjective extends PrivateObjective {
    private static ShadesOfPurpleObjective instance = null;
    final private static Color color = Color.PURPLE;

    private ShadesOfPurpleObjective(String imagePath, String title){
        super(imagePath,title);
    }
    private synchronized static ShadesOfPurpleObjective createInstance(String imagePath, String title){
        if (instance==null) instance = new ShadesOfPurpleObjective(imagePath, title);
        return instance;
    }

    public static ShadesOfPurpleObjective instance(String imagePath, String title){
        if (instance==null) createInstance(imagePath, title);
        return instance;
    }
    public int evalPoints(Player player){
        return (int) StreamSupport.stream(player.getMap().spliterator(),false)
                .filter(square -> square.getDie().getColor()==color)
                .count();
    }
}
