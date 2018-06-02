package it.polimi.se2018.client.view.gui.button;

import it.polimi.se2018.client.view.gui.stateGUI.State;
import it.polimi.se2018.mvc.controller.ModelView;
import javafx.scene.control.Button;

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
        usable = false;
    }

    @Override
    public void checkCondition(ButtonCheckUsabilityHandler handler, State currentState){
        usable = handler.checkUsability(this, currentState);
        if(usable) arm();
        else disarm();
    }
}