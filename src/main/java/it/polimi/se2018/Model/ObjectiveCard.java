package it.polimi.se2018.Model;

public abstract class ObjectiveCard {
    private String imagePath;
    private String title;

    public ObjectiveCard(String imagePath, String title) {
        this.imagePath=imagePath;
        this.title=title;
    }

    public int evalPoints(Player player) {
        return 0;
    } //evaluates points based on the effect of the card


}
