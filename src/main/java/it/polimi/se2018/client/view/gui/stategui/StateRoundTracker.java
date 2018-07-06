package it.polimi.se2018.client.view.gui.stategui;

import it.polimi.se2018.client.view.gui.controllers.GameSceneController;
import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityRoundTracker;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateWindow;
import it.polimi.se2018.network.messages.Coordinate;
import javafx.application.Platform;

/**
 * This state allow the player to press the round tracker buttons, pass buttons and toolcards buttons.
 * It's reach during the usage of a toolcard
 */
public class StateRoundTracker extends StateWindow {

    public StateRoundTracker(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
        buttonCheckUsabilityHandler = new ButtonCheckUsabilityRoundTracker(gameSceneController);
        descriptionOfState = "Select a die from the Round Tracker";
    }

    @Override
    public void doActionWindow(Coordinate coordinate) {
        gameSceneController.getToolCardMessage().addRoundTrackerPosition(coordinate);
        Platform.runLater(() -> {
            changeState(new StateTurn(gameSceneController));
            gameSceneController.disableAllButton();
        });
        gameSceneController.sendToolCardMessage();
    }

    @Override
    public void doActionDraftPool(int draftPoolPosition) {
        //This has to be empty
    }

    @Override
    public void doActionToolCard(int toolCardIndex) {
        if (toolCardIndex == gameSceneController.getToolCardMessage().getToolCardNumber()) {
            gameSceneController.setToolCardMessage(null);
            Platform.runLater(() -> {
                changeState(new StateTurn(gameSceneController));
                gameSceneController.setAllButton();
            });
        }
    }
}
