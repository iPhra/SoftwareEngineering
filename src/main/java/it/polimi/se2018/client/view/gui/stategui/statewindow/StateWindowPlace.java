package it.polimi.se2018.client.view.gui.stategui.statewindow;

import it.polimi.se2018.client.view.gui.GameSceneController;
import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityWindow;
import it.polimi.se2018.client.view.gui.stategui.StateTurn;
import it.polimi.se2018.network.messages.Coordinate;
import javafx.application.Platform;

public class StateWindowPlace extends StateWindow {
    public StateWindowPlace(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
        buttonCheckUsabilityHandler = new ButtonCheckUsabilityWindow(gameSceneController);
        descriptionOfState = "Select where place your die in hand";
    }

    @Override
    public void doActionWindow(Coordinate coordinate) {
        /*PlaceMessage placeMessage = new PlaceMessage(gameSceneController.getPlayerID(), gameSceneController.getGuiModel().getBoard().getStateID(), coordinate);
        gameSceneController.getGuiView().handleNetworkOutput(placeMessage);
        */
        gameSceneController.getToolCardMessage().addFinalPosition(coordinate);
        gameSceneController.sendToolCardMessage();
        Platform.runLater(() -> {
            changeState(new StateTurn(gameSceneController));
            gameSceneController.disableAllButton();
        });
    }

    @Override
    public void doActionDraftPool(int draftPoolPosition) {
        //To do something?
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