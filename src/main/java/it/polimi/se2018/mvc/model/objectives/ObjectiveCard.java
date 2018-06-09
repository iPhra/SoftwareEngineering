package it.polimi.se2018.mvc.model.objectives;

import it.polimi.se2018.mvc.model.Player;

import java.io.Serializable;

/**
 * This is the abstract objective card.
 * It will be extended by private objective cards and public objective cards
 */
public abstract class ObjectiveCard implements Serializable{

    /**
     * This is the title of the card
     */
    private final String title;

    /**
     * This is the description of the card
     */
    private final String description;

    /**
     * This is the imagePath of the card
     */
    private final String imagePath;

    protected ObjectiveCard(String title, String description, String imagePath) {
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
    }

    /**
     * @return the title of this card
     */
    protected String getTitle() {
        return title;
    }

    /**
     * @return the description of this card
     */
    protected String getDescription() {
        return description;
    }

    /**
     * @return the imagePath of this card
     */
    public String getImagePath() {return imagePath;}

    /**
     * Evaluates points based on the effect of the card
     * @param player the player whose points must be evaluated
     * @return the points given by this card to the player
     */
    public abstract int evalPoints(Player player);
}
