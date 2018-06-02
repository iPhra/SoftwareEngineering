package it.polimi.se2018.client.view.gui.stateGUI.stateWindow;

import it.polimi.se2018.client.view.gui.GameSceneController;
import it.polimi.se2018.client.view.gui.stateGUI.State;
import it.polimi.se2018.client.view.gui.stateGUI.StateTurn;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.PlaceMessage;

import java.util.List;

public class StateWindowPlace extends StateWindow {
    public StateWindowPlace(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    @Override
    public void doActionWindow(Coordinate coordinate) {
        PlaceMessage placeMessage = new PlaceMessage(gameSceneController.getPlayerID(), gameSceneController.getStateID(), coordinate);
        //TODO send message
        changeState(new StateTurn(gameSceneController));
        gameSceneController.disableAllButton();
    }
}