package it.polimi.se2018.client.view.gui.stategui;

import it.polimi.se2018.client.view.gui.controllers.GameSceneController;
import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityTurn;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.DraftMessage;
import it.polimi.se2018.network.messages.requests.PlaceMessage;
import it.polimi.se2018.network.messages.requests.ToolCardRequestMessage;
import javafx.application.Platform;

/**
 * This state is the normal state of the game when is your turn and the player is not using a toolcard
 */
public class StateTurn extends State {

    public StateTurn(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
        buttonCheckUsabilityHandler = new ButtonCheckUsabilityTurn(gameSceneController);
        descriptionOfState = "Choose your action";
    }

    @Override
    public void doActionWindow(Coordinate coordinate) {
        Platform.runLater(() -> {
            changeState(new StateTurn(gameSceneController));
            gameSceneController.disableAllButton();
        });
        gameSceneController.getGuiView().handleNetworkOutput(new PlaceMessage(gameSceneController.getPlayerID(), gameSceneController.getGuiModel().getBoard().getStateID(), coordinate));
    }

    @Override
    public void doActionDraftPool(int draftPoolPosition) {
        Platform.runLater(() -> {
            changeState(new StateTurn(gameSceneController));
            gameSceneController.disableAllButton();
        });
        gameSceneController.getGuiView().handleNetworkOutput(new DraftMessage(gameSceneController.getPlayerID(), gameSceneController.getGuiModel().getBoard().getStateID(), draftPoolPosition));
    }

    @Override
    public void doActionToolCard(int toolCardIndex) {
        Platform.runLater(() -> {
            changeState(new StateTurn(gameSceneController));
            gameSceneController.disableAllButton();
        });
        gameSceneController.getGuiView().handleNetworkOutput(new ToolCardRequestMessage(gameSceneController.getPlayerID(), gameSceneController.getGuiModel().getBoard().getStateID(), toolCardIndex));
    }
}
