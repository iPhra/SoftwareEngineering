package it.polimi.se2018.mvc.model.objectives.publicobjectives;

import it.polimi.se2018.mvc.model.objectives.ObjectiveCard;

/**
 * This is the abstract Public Objective card.
 */
public abstract class PublicObjective extends ObjectiveCard {

    PublicObjective(String title, String description, String imagePath) {
        super(title,description,imagePath);
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
