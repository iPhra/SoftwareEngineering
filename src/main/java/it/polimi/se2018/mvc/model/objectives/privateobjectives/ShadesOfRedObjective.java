package it.polimi.se2018.mvc.model.objectives.privateobjectives;

import it.polimi.se2018.mvc.model.Color;

/**
 * This is the shades of red objective card. It extends {@link PrivateObjective}
 * Design pattern Singleton is used in this class, because only one instance is used in every running game
 * @author Emilio Imperiali
 */
public class ShadesOfRedObjective extends PrivateObjective {
    private static ShadesOfRedObjective instance = null;
    private static final Color color = Color.RED;

    private ShadesOfRedObjective(String title){
        super(title,color);
        description = "Sum of values on red dice";
    }

    /**
     * @param title it's the title of this card
     * @return a new instance of this card if does not exist, the existing instance otherwise (as expected in the
     * Singleton pattern)
     */
    public static ShadesOfRedObjective instance(String title){
        if (instance==null) instance = new ShadesOfRedObjective(title);
        return instance;
    }
}
