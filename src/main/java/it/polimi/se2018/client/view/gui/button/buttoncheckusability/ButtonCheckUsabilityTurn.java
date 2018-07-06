package it.polimi.se2018.client.view.gui.button.buttoncheckusability;

import it.polimi.se2018.client.view.gui.controllers.GameSceneController;
import it.polimi.se2018.client.view.gui.button.*;

/**
 * This class is used to able and disable buttons of the GUI according to the current state.
 * The state is on when is the turn of the player and he is not using a toolcard
 */
public class ButtonCheckUsabilityTurn implements ButtonCheckUsabilityHandler {
    private final GameSceneController gameSceneController;

    public ButtonCheckUsabilityTurn(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    private Boolean checkTurn() {
        return gameSceneController.getGuiModel().getBoard().getCurrentPlayerID() == gameSceneController.getPlayerID();
    }

    @Override
    public boolean checkUsability(ButtonSquare buttonSquare) {
        return (checkTurn() && (gameSceneController.getGuiModel().getBoard().hasDieInHand()));
    }

    @Override
    public boolean checkUsability(ButtonDraftPool buttonDraftPool) {
        return (checkTurn() && (!gameSceneController.getGuiModel().getBoard().hasDraftedDie()));
    }

    @Override
    public boolean checkUsability(ButtonGame buttonGame) {
        return checkTurn();
    }

    @Override
    public boolean checkUsability(ButtonToolCard buttonToolCard) {
        return (checkTurn() && !gameSceneController.getGuiModel().getBoard().hasUsedCard() && (gameSceneController.getGuiModel().getBoard().getToolCardUsability().get(buttonToolCard.getToolCardNumber())));
    }

    @Override
    public boolean checkUsability(MenuItemRoundTracker menuItemRoundTracker) {
        return false;
    }
}
