package it.polimi.se2018.client.view.gui.button.buttoncheckusability;

import it.polimi.se2018.client.view.gui.GameSceneController;
import it.polimi.se2018.client.view.gui.button.*;

public class ButtonCheckUsabilityDraftPool implements ButtonCheckUsabilityHandler {
    private GameSceneController gameSceneController;

    public ButtonCheckUsabilityDraftPool(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    private Boolean checkTurn() {
        return gameSceneController.getModelView().getCurrentPlayerID() == gameSceneController.getPlayerID();
    }

    @Override
    public Boolean checkUsability(ButtonSquare buttonSquare) {
        return false;
    }

    @Override
    public Boolean checkUsability(ButtonDraftPool buttonDraftPool) {
        return checkTurn();
    }

    @Override
    public Boolean checkUsability(ButtonGame buttonGame) {
        return checkTurn();
    }

    @Override
    public Boolean checkUsability(ButtonToolCard buttonToolCard) {
        //TODO
        return checkTurn();
    }

    @Override
    public Boolean checkUsability(ButtonRoundTracker buttonRoundTracker) {
        return false;
    }
}
