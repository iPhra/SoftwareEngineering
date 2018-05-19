package it.polimi.se2018.model.objectives;

import it.polimi.se2018.model.Player;

import java.io.Serializable;

public abstract class ObjectiveCard implements Serializable{
    protected final String title;
    protected String description;

    protected ObjectiveCard(String title) {
        this.title=title;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    //evaluates points based on the effect of the card
    public abstract int evalPoints(Player player);


}
