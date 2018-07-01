package it.polimi.se2018.mvc.model.objectives.privateobjectives;

import it.polimi.se2018.mvc.model.Color;

/**
 * This is the shades of green objective card. It extends {@link PrivateObjective}
 * Design pattern Singleton is used in this class, because only one instance is used in every running game
 */
public class ShadesOfGreenObjective extends PrivateObjective {
    private static ShadesOfGreenObjective instance = null;
    private static final Color color = Color.GREEN;

    private ShadesOfGreenObjective(String imagePath){
        super("Shades of Green","Sum of values on green dice",imagePath,color);
    }

    /**
     * @return a new instance of this card if does not exist, the existing instance otherwise (as expected in the
     * Singleton pattern)
     */
    public static ShadesOfGreenObjective instance(){
        if (instance==null) instance = new ShadesOfGreenObjective("/objectives/private_objectives/shades_of_green.png");
        return instance;
    }
}
