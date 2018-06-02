package it.polimi.se2018.client.view.gui.button;

import it.polimi.se2018.client.view.gui.stateGUI.State;
import it.polimi.se2018.mvc.controller.ModelView;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.network.messages.Coordinate;
import javafx.scene.control.Button;

public class ButtonSquare extends ButtonGame {
    private final int playerID;
    private final Coordinate coordinate;
    private Boolean usable;

    public ButtonSquare(int playerID, Coordinate coordinate) {
        this.playerID = playerID;
        this.coordinate = coordinate;
        usable = false;
    }

    @Override
    public void checkCondition(ButtonCheckUsabilityHandler handler, State currentState){
        usable = handler.checkUsability(this, currentState);
        if(usable) arm();
        else disarm();
    }
}