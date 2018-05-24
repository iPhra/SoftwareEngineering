package it.polimi.se2018.mvc.model.objectives;

import it.polimi.se2018.mvc.model.Player;

import java.io.Serializable;

/**
 * This is the abstract objective card. It will be extended by private objective cards and public objective cards
 * @author Emilio Imperiali
 */
public abstract class ObjectiveCard implements Serializable{

    /**
     * This is the title of the card
     */
    protected final String title;

    /**
     * This is the description of the card
     */
    protected String description;

    protected ObjectiveCard(String title) {
        this.title=title;
    }

    /**
     * @return the title of this card
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the description of this card
     */
    public String getDescription() {
        return description;
    }

    /**
     * Evaluates points based on the effect of the card
     * @param player the player whose points must be evaluated
     * @return the points given by this card to the player
     */
    public abstract int evalPoints(Player player);
}
