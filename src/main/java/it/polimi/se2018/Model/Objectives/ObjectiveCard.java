package it.polimi.se2018.Model.Objectives;

import it.polimi.se2018.Model.Player;

public abstract class ObjectiveCard {
    protected String imagePath;
    protected String title;

    protected ObjectiveCard(String imagePath, String title) {
        this.imagePath=imagePath;
        this.title=title;
    }

    //evaluates points based on the effect of the card
    public abstract int evalPoints(Player player);


}
