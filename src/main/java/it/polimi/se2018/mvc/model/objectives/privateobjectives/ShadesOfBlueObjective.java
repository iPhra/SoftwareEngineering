package it.polimi.se2018.mvc.model.objectives.privateobjectives;

import it.polimi.se2018.mvc.model.Color;

/**
 * This is the shades of blue objective card. It extends {@link PrivateObjective}
 * Design pattern Singleton is used in this class, because only one instance is used in every running game
 */
public class ShadesOfBlueObjective extends PrivateObjective {
    private static ShadesOfBlueObjective instance = null;
    private static final Color color = Color.BLUE;

    private ShadesOfBlueObjective(String imagePath){
        super("Shades of Blue","Sum of values on blue dice", imagePath,color);
    }

    /**
     * @return a new instance of this card if does not exist, the existing instance otherwise (as expected in the
     * Singleton pattern)
     */
    public static ShadesOfBlueObjective instance(String imagePath){
        if (instance==null) instance = new ShadesOfBlueObjective(imagePath);
        return instance;
    }
}