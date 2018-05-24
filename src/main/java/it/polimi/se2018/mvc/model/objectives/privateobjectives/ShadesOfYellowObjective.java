package it.polimi.se2018.mvc.model.objectives.privateobjectives;

import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;

import java.util.Objects;
import java.util.stream.StreamSupport;

/**
 * This is the shades of yellow objective card. It extends {@link PrivateObjective}
 * Design pattern Singleton is used in this class, because only one instance is used in every running game
 * @author Emilio Imperiali
 */
public class ShadesOfYellowObjective extends PrivateObjective {
    private static ShadesOfYellowObjective instance = null;
    private static final Color color = Color.YELLOW;

    private ShadesOfYellowObjective(String title){
        super(title,color);
        description = "Sum of values on yellow dice";
    }

    /**
     * @param title it's the title of this card
     * @return a new instance of this card if does not exist, the existing instance otherwise (as expected in the
     * Singleton pattern)
     */
    public static ShadesOfYellowObjective instance(String title){
        if (instance==null) instance = new ShadesOfYellowObjective(title);
        return instance;
    }
}