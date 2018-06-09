package it.polimi.se2018.client.view.gui.stategui.statewindow;

import it.polimi.se2018.client.view.gui.GameSceneController;
import it.polimi.se2018.client.view.gui.stategui.StateTurn;
import it.polimi.se2018.network.messages.Coordinate;

public class StateWindowPlace extends StateWindow {
    public StateWindowPlace(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    @Override
    public void doActionWindow(Coordinate coordinate) {
        //PlaceMessage placeMessage = new PlaceMessage(gameSceneController.getPlayerID(), gameSceneController.getStateID(), coordinate);
        //send message
        changeState(new StateTurn(gameSceneController));
        gameSceneController.disableAllButton();
    }
}