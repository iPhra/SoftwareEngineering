package it.polimi.se2018.Model.Objectives.PublicObjectives;

import it.polimi.se2018.Model.Player;

public class ColorDiagonalsObjectives extends PublicObjective {
    private static ColorDiagonalsObjectives instance = null;

    private ColorDiagonalsObjectives(String imagePath, String title){
        super(imagePath,title);
    }
    private synchronized static ColorDiagonalsObjectives createInstance(String imagePath, String title){
        if (instance==null) instance = new ColorDiagonalsObjectives(imagePath, title);
        return instance;
    }

    public static ColorDiagonalsObjectives instance(String imagePath, String title){
        if (instance==null) createInstance(imagePath, title);
        return instance;
    }

    public int evalPoints(Player player){return 0;} //i write return 0 because otherwise it won't compile. this method has to be implemented

}
