package it.polimi.se2018.mvc.model.objectives.privateobjectives;

import it.polimi.se2018.mvc.model.Color;

/**
 * This is the shades of yellow objective card. It extends {@link PrivateObjective}
 * Design pattern Singleton is used in this class, because only one instance is used in every running game
 */
public class ShadesOfYellowObjective extends PrivateObjective {
    private static ShadesOfYellowObjective instance = null;
    private static final Color color = Color.YELLOW;

    private ShadesOfYellowObjective(String imagePath){
        super("Shades of Yellow","Sum of values on yellow dice",imagePath,color);
    }

    /**
     * @return a new instance of this card if does not exist, the existing instance otherwise (as expected in the
     * Singleton pattern)
     */
    public static ShadesOfYellowObjective instance(String imagePath){
        if (instance==null) instance = new ShadesOfYellowObjective(imagePath);
        return instance;
    }
}