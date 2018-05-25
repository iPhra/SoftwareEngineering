package it.polimi.se2018.mvc.model.objectives.publicobjectives;

import it.polimi.se2018.mvc.model.objectives.ObjectiveCard;

/**
 * This is the abstract public objective card. it extends {@link ObjectiveCard}
 * @author Emilio Imperiali
 */
public abstract class PublicObjective extends ObjectiveCard {

    protected PublicObjective(String title) {
        super(title);
    }

    @Override
    public String toString() {
        return "Title: \"" + getTitle() + "\", Effect: \"" + getDescription() + "\"" + "\n";
    }
}
