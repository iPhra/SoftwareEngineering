package it.polimi.se2018.mvc.model.objectives.publicobjectives;

import it.polimi.se2018.mvc.model.objectives.ObjectiveCard;

/**
 * This is the abstract public objective card. it extends {@link ObjectiveCard}
 */
public abstract class PublicObjective extends ObjectiveCard {

    PublicObjective(String title) {
        super(title);
    }

    @Override
    public String toString() {
        StringBuilder spaces = new StringBuilder();
        for(int i=0; i<21-getTitle().length()-1; i++) {
            spaces.append(" ");
        }
        spaces.append("      ");
        return "Title: \"" + getTitle() +"\"" +  spaces.toString() + "Effect: \"" + getDescription() + "\"" + "\n";
    }
}
