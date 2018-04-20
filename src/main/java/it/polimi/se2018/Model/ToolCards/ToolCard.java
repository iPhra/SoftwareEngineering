package it.polimi.se2018.Model.ToolCards;

abstract public class ToolCard {
    private boolean alreadyUsed; //true if this tool card has already been used once
    private boolean usableAfterDraft; //true if this tool card is usable after placing a die
    private String imagePath;
    private String title;

    public ToolCard(String imagePath, String title) {
        this.imagePath=imagePath;
        this.title=title;
        alreadyUsed = false;
    }

    public void useCard() { //every specific tool card will implement this method differently
    }

}
