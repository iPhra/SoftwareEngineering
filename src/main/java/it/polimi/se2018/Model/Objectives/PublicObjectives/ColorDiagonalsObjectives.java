package it.polimi.se2018.Model.Objectives.PublicObjectives;

import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Model.Map;
import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Model.Square;

import java.util.ArrayList;

public class ColorDiagonalsObjectives extends PublicObjective {
    private static ColorDiagonalsObjectives instance = null;

    private ColorDiagonalsObjectives(String imagePath, String title){
        super(imagePath,title);
    }
    private static synchronized ColorDiagonalsObjectives createInstance(String imagePath, String title){
        if (instance==null) instance = new ColorDiagonalsObjectives(imagePath, title);
        return instance;
    }

    public static ColorDiagonalsObjectives instance(String imagePath, String title){
        if (instance==null) createInstance(imagePath, title);
        return instance;
    }

    @Override
    public int evalPoints(Player player){
        Map map = player.getMap();
        int points = 0;
        for (Square square : map) {
            if (square.getDie() != null) {
                ArrayList<Die> belowDiagonalsDice = map.belowDiagonalsDice(square.getRow(),square.getCol());
                for (Die die : belowDiagonalsDice) {
                    if (die.getColor() == square.getDie().getColor()) points++;
                }
            }
        }
        return points;
    }

}
