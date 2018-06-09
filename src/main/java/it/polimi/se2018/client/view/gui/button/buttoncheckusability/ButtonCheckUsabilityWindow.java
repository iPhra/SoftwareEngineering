package it.polimi.se2018.client.view.gui.button.buttoncheckusability;

import it.polimi.se2018.client.view.gui.GameSceneController;
import it.polimi.se2018.client.view.gui.button.*;

public class ButtonCheckUsabilityWindow implements ButtonCheckUsabilityHandler {
    private GameSceneController gameSceneController;

    public ButtonCheckUsabilityWindow(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    private Boolean checkTurn() {
        return gameSceneController.getModelView().getCurrentPlayerID() == gameSceneController.getPlayerID();
    }

    @Override
    public Boolean checkUsability(ButtonSquare buttonSquare) {
        return checkTurn();
    }

    @Override
    public Boolean checkUsability(ButtonDraftPool buttonDraftPool) {
        return false;
    }

    @Override
    public Boolean checkUsability(ButtonGame buttonGame) {
        return checkTurn();
    }

    @Override
    public Boolean checkUsability(ButtonToolCard buttonToolCard) {
        //TODO adesso funziona così: se sto usando una toolcarda posso schiacciare su tutte le toolcard
        //se premo su un'altra toolcard non succede niente, se premom su quella scelta precedentemente
        //butto tutte le selezioni effettuate e faccio ripartire le scelte iniziali, come nella CLi
        return checkTurn();
    }

    @Override
    public Boolean checkUsability(ButtonRoundTracker buttonRoundTracker) {
        return false;
    }
}
