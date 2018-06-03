package it.polimi.se2018.mvc.model.objectives.privateobjectives;

import it.polimi.se2018.mvc.model.Color;

/**
 * This is the shades of yellow objective card. It extends {@link PrivateObjective}
 * Design pattern Singleton is used in this class, because only one instance is used in every running game
 * @author Emilio Imperiali
 */
public class ShadesOfYellowObjective extends PrivateObjective {
    private static ShadesOfYellowObjective instance = null;
    private static final Color color = Color.YELLOW;

    private ShadesOfYellowObjective(String title, String imagePath){
        super(title,color);
        description = "Sum of values on yellow dice";
        this.imagePath = imagePath;
    }

    /**
     * @param title it's the title of this card
     * @return a new instance of this card if does not exist, the existing instance otherwise (as expected in the
     * Singleton pattern)
     */
    public static ShadesOfYellowObjective instance(String title, String imagePath){
        if (instance==null) instance = new ShadesOfYellowObjective(title,imagePath);
        return instance;
    }
}