package it.polimi.se2018.mvc.model.objectives.privateobjectives;

import it.polimi.se2018.mvc.model.Color;

/**
 * This is the shades of purple objective card. It extends {@link PrivateObjective}
 * Design pattern Singleton is used in this class, because only one instance is used in every running game
 * @author Emilio Imperiali
 */
public class ShadesOfPurpleObjective extends PrivateObjective {
    private static ShadesOfPurpleObjective instance = null;
    private static final Color color = Color.PURPLE;

    private ShadesOfPurpleObjective(String title, String imagePath){
        super(title,color);
        description = "Sum of values on purple dice";
        this.imagePath = imagePath;
    }

    /**
     * @param title it's the title of this card
     * @return a new instance of this card if does not exist, the existing instance otherwise (as expected in the
     * Singleton pattern)
     */
    public static ShadesOfPurpleObjective instance(String title, String imagePath){
        if (instance==null) instance = new ShadesOfPurpleObjective(title, imagePath);
        return instance;
    }
}
