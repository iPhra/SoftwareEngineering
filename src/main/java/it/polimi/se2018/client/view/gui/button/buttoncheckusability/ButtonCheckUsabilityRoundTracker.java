package it.polimi.se2018.client.view.gui.button.buttoncheckusability;

import it.polimi.se2018.client.view.gui.GameSceneController;
import it.polimi.se2018.client.view.gui.button.*;

public class ButtonCheckUsabilityRoundTracker implements ButtonCheckUsabilityHandler {
    private final GameSceneController gameSceneController;

    public ButtonCheckUsabilityRoundTracker(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    private Boolean checkTurn() {
        return gameSceneController.getModelView().getCurrentPlayerID() == gameSceneController.getPlayerID();
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
        //implement
        return checkTurn();
    }

    @Override
    public boolean checkUsability(ButtonRoundTracker buttonRoundTracker) {
        return checkTurn();
    }
}
