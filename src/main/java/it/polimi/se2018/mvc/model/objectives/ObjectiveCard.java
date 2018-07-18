package it.polimi.se2018.mvc.model.objectives;

import it.polimi.se2018.mvc.model.Player;

import java.io.Serializable;

/**
 * This is the abstract Objective Card.
 */
public abstract class ObjectiveCard implements Serializable{
    private final String title;
    private final String description;
    private final String imagePath;

    protected ObjectiveCard(String title, String description, String imagePath) {
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
    }

    protected String getTitle() {
        return title;
    }

    protected String getDescription() {
        return description;
    }

    public String getImagePath() {return imagePath;}

    /**
     * Evaluates points based on the effect of the card
     * @param player the player whose points must be evaluated
     * @return the points given by this card to the player
     */
    public abstract int evalPoints(Player player);
}
