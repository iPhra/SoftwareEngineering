package it.polimi.se2018.client.view.gui.button.buttoncheckusability;

import it.polimi.se2018.client.view.gui.GameSceneController;
import it.polimi.se2018.client.view.gui.button.*;

public class ButtonCheckUsabilityTurn implements ButtonCheckUsabilityHandler {
    private GameSceneController gameSceneController;

    public ButtonCheckUsabilityTurn(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    private Boolean checkTurn() {
        return gameSceneController.getModelView().getCurrentPlayerID() == gameSceneController.getPlayerID();
    }

    @Override
    public Boolean checkUsability(ButtonSquare buttonSquare) {
        return (checkTurn() && (!gameSceneController.getModelView().hasDieInHand()));
    }

    @Override
    public Boolean checkUsability(ButtonDraftPool buttonDraftPool) {
        return (checkTurn() && (!gameSceneController.getModelView().hasDraftedDie()));
    }

    @Override
    public Boolean checkUsability(ButtonGame buttonGame) {
        return checkTurn();
    }

    @Override
    public Boolean checkUsability(ButtonToolCard buttonToolCard) {
        return (checkTurn() && (gameSceneController.getModelView().getToolCardUsability().get(buttonToolCard.getNumberOfToolCard())));
    }

    @Override
    public Boolean checkUsability(ButtonRoundTracker buttonRoundTracker) {
        return false;
    }
}
