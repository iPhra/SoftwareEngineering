package it.polimi.se2018.client.view.gui.button;

import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityHandler;

public class ButtonToolCard extends ButtonGame {
    private final int playerID;
    private Boolean usable;
    private int numberOfToolCard;

    public int getNumberOfToolCard() {
        return numberOfToolCard;
    }

    public ButtonToolCard(int playerID, int numberOfToolCard) {
        this.playerID = playerID;
        this.numberOfToolCard = numberOfToolCard;
        disarm();
    }

    @Override
    public void checkCondition(ButtonCheckUsabilityHandler handler){
        usable = handler.checkUsability(this);
        if(usable) arm();
        else disarm();
    }
}