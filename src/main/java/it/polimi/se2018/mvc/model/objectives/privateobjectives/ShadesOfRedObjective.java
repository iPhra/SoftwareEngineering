package it.polimi.se2018.mvc.model.objectives.privateobjectives;

import it.polimi.se2018.mvc.model.Color;

/**
 * This is the shades of red objective card. It extends {@link PrivateObjective}
 * Design pattern Singleton is used in this class, because only one instance is used in every running game
 */
public class ShadesOfRedObjective extends PrivateObjective {
    private static ShadesOfRedObjective instance = null;
    private static final Color color = Color.RED;

    private ShadesOfRedObjective(String imagePath){
        super("Shades of Red","Sum of values on red dice",imagePath,color);
    }

    /**
     * @return a new instance of this card if does not exist, the existing instance otherwise (as expected in the
     * Singleton pattern)
     */
    public static ShadesOfRedObjective instance(){
        if (instance==null) instance = new ShadesOfRedObjective("/objectives/private_objectives/shades_of_red.png");
        return instance;
    }
}
