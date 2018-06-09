package it.polimi.se2018.client.view.gui.button;

import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityHandler;

public class ButtonToolCard extends ButtonGame {
    private final int playerID;
    private Boolean usable;
    private final int toolCardNumber;

    public int getToolCardNumber() {
        return toolCardNumber;
    }

    public ButtonToolCard(int playerID, int toolCardNumber) {
        this.playerID = playerID;
        this.toolCardNumber = toolCardNumber;
        disarm();
    }

    @Override
    public void checkCondition(ButtonCheckUsabilityHandler handler){
        usable = handler.checkUsability(this);
        if(usable) arm();
        else disarm();
    }
}