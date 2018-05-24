package it.polimi.se2018.mvc.model.objectives.privateobjectives;

import it.polimi.se2018.mvc.model.objectives.ObjectiveCard;

/**
 * This is the abstract private objective card. it extends {@link ObjectiveCard}
 * @author Emilio Imperiali
 */
public abstract class PrivateObjective extends ObjectiveCard {

    protected PrivateObjective(String title) {
        super(title);
    }

}
