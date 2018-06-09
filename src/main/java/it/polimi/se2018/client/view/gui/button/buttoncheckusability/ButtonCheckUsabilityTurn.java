package it.polimi.se2018.client.view.gui.button.buttoncheckusability;

import it.polimi.se2018.client.view.gui.GameSceneController;
import it.polimi.se2018.client.view.gui.button.*;

public class ButtonCheckUsabilityTurn implements ButtonCheckUsabilityHandler {
    private final GameSceneController gameSceneController;

    public ButtonCheckUsabilityTurn(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    private Boolean checkTurn() {
        return gameSceneController.getModelView().getCurrentPlayerID() == gameSceneController.getPlayerID();
    }

    @Override
    public boolean checkUsability(ButtonSquare buttonSquare) {
        return (checkTurn() && (!gameSceneController.getModelView().hasDieInHand()));
    }

    @Override
    public boolean checkUsability(ButtonDraftPool buttonDraftPool) {
        return (checkTurn() && (!gameSceneController.getModelView().hasDraftedDie()));
    }

    @Override
    public boolean checkUsability(ButtonGame buttonGame) {
        return checkTurn();
    }

    @Override
    public boolean checkUsability(ButtonToolCard buttonToolCard) {
        return (checkTurn() && (gameSceneController.getModelView().getToolCardUsability().get(buttonToolCard.getToolCardNumber())));
    }

    @Override
    public boolean checkUsability(ButtonRoundTracker buttonRoundTracker) {
        return false;
    }
}
