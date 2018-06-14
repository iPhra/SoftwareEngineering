package it.polimi.se2018.client.view.gui.button.buttoncheckusability;

import it.polimi.se2018.client.view.gui.GameSceneController;
import it.polimi.se2018.client.view.gui.button.*;

public class ButtonCheckUsabilityDraftPool implements ButtonCheckUsabilityHandler {
    private final GameSceneController gameSceneController;

    public ButtonCheckUsabilityDraftPool(GameSceneController gameSceneController) {
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
        return checkTurn();
    }

    @Override
    public boolean checkUsability(ButtonGame buttonGame) {
        return checkTurn();
    }

    @Override
    public boolean checkUsability(ButtonToolCard buttonToolCard) {
        //implmenet
        return checkTurn();
    }

    @Override
    public boolean checkUsability(MenuItemRoundTracker menuItemRoundTracker) {
        return false;
    }
}
