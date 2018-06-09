package it.polimi.se2018.client.view.gui.button;

import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityHandler;
import it.polimi.se2018.network.messages.Coordinate;

public class ButtonRoundTracker extends ButtonGame {
    private final int playerID;
    private final Coordinate coordinate;
    private Boolean usable;

    public ButtonRoundTracker(int playerID, Coordinate coordinate) {
        this.playerID = playerID;
        this.coordinate = coordinate;
        disarm();
    }

    @Override
    public void checkCondition(ButtonCheckUsabilityHandler handler){
        usable = handler.checkUsability(this);
        if(usable) arm();
        else disarm();
    }
}
