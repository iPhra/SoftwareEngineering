package it.polimi.se2018.mvc.model.objectives.privateobjectives;

import it.polimi.se2018.mvc.model.Color;

/**
 * This is the shades of blue objective card. It extends {@link PrivateObjective}
 * Design pattern Singleton is used in this class, because only one instance is used in every running game
 */
public class ShadesOfBlueObjective extends PrivateObjective {
    private static ShadesOfBlueObjective instance = null;
    private static final Color color = Color.BLUE;

    private ShadesOfBlueObjective(String title,String imagePath){
        super(title,color);
        description = "Sum of values on blue dice";
        this.imagePath = imagePath;
    }

    /**
     * @param title it's the title of this card
     * @return a new instance of this card if does not exist, the existing instance otherwise (as expected in the
     * Singleton pattern)
     */
    public static ShadesOfBlueObjective instance(String title, String imagePath){
        if (instance==null) instance = new ShadesOfBlueObjective(title,imagePath);
        return instance;
    }
}