package it.polimi.se2018.model.objectives;

import it.polimi.se2018.model.Player;

public abstract class ObjectiveCard {
    protected final String imagePath;
    protected final String title;

    protected ObjectiveCard(String imagePath, String title) {
        this.imagePath=imagePath;
        this.title=title;
    }

    //evaluates points based on the effect of the card
    public abstract int evalPoints(Player player);


}