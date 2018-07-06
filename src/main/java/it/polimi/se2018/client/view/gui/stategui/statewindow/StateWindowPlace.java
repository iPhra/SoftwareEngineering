package it.polimi.se2018.client.view.gui.stategui.statewindow;

import it.polimi.se2018.client.view.gui.controllers.GameSceneController;
import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityWindow;
import it.polimi.se2018.client.view.gui.stategui.StateTurn;
import it.polimi.se2018.network.messages.Coordinate;
import javafx.application.Platform;

/**
 * This state is reach during the usage of {@link it.polimi.se2018.mvc.model.toolcards.CorkBackedStraightedge}
 */
public class StateWindowPlace extends StateWindow {
    public StateWindowPlace(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
        buttonCheckUsabilityHandler = new ButtonCheckUsabilityWindow(gameSceneController);
        descriptionOfState = "Select where place your die in hand";
    }

    @Override
    public void doActionWindow(Coordinate coordinate) {
        gameSceneController.getToolCardMessage().addFinalPosition(coordinate);
        Platform.runLater(() -> {
            changeState(new StateTurn(gameSceneController));
            gameSceneController.disableAllButton();
        });
        gameSceneController.sendToolCardMessage();
    }
}