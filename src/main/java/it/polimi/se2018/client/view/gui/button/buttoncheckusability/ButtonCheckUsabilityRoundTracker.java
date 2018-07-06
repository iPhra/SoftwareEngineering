package it.polimi.se2018.client.view.gui.button.buttoncheckusability;

import it.polimi.se2018.client.view.gui.controllers.GameSceneController;
import it.polimi.se2018.client.view.gui.button.*;

/**
 * This class is used to able and disable buttons of the GUI according to the current state (RoundTrackerState)
 */
public class ButtonCheckUsabilityRoundTracker implements ButtonCheckUsabilityHandler {
    private final GameSceneController gameSceneController;

    public ButtonCheckUsabilityRoundTracker(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    private Boolean checkTurn() {
        return gameSceneController.getGuiModel().getBoard().getCurrentPlayerID() == gameSceneController.getPlayerID();
    }

    @Override
    public boolean checkUsability(ButtonSquare buttonSquare) {
        return false;
    }

    @Override
    public boolean checkUsability(ButtonDraftPool buttonDraftPool) {
        return false;
    }

    @Override
    public boolean checkUsability(ButtonGame buttonGame) {
        return checkTurn();
    }

    @Override
    public boolean checkUsability(ButtonToolCard buttonToolCard) {
        return checkTurn() && gameSceneController.getToolCardMessage().getToolCardNumber()==buttonToolCard.getToolCardNumber();
    }

    @Override
    public boolean checkUsability(MenuItemRoundTracker menuItemRoundTracker) {
        return checkTurn();
    }
}
