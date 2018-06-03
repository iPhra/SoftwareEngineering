package it.polimi.se2018.client.view.gui.button;

import it.polimi.se2018.client.view.gui.GameSceneController;
import it.polimi.se2018.client.view.gui.stategui.State;
import it.polimi.se2018.client.view.gui.stategui.StateRoundTracker;
import it.polimi.se2018.client.view.gui.stategui.StateTurn;
import it.polimi.se2018.client.view.gui.stategui.statedraftpool.StateToolCardDraft;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateWindowEnd;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateWindowPlace;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateWindowStart;

public class ButtonCheckUsability implements ButtonCheckUsabilityHandler {
    private GameSceneController gameSceneController;

    public ButtonCheckUsability(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    private Boolean checkTurn() {
        return gameSceneController.getModelView().getCurrentPlayerID() == gameSceneController.getPlayerID();
    }

    @Override
    public Boolean checkUsability(ButtonSquare buttonSquare, StateToolCardDraft stateToolCardDraft) {
        return false;
    }

    @Override
    public Boolean checkUsability(ButtonSquare buttonSquare, StateWindowEnd stateWindowEnd) {
        return checkTurn();
    }

    @Override
    public Boolean checkUsability(ButtonSquare buttonSquare, StateWindowPlace stateWindowPlace) {
        return checkTurn();
    }

    @Override
    public Boolean checkUsability(ButtonSquare buttonSquare, StateWindowStart stateWindowStart) {
        return checkTurn();
    }

    @Override
    public Boolean checkUsability(ButtonSquare buttonSquare, StateRoundTracker stateRoundTracker) {
        return false;
    }

    @Override
    public Boolean checkUsability(ButtonSquare buttonSquare, StateTurn stateTurn) {
        return (checkTurn() && (!gameSceneController.getModelView().hasDieInHand()));
    }

    @Override
    public Boolean checkUsability(ButtonDraftPool buttonDraftPool, StateToolCardDraft stateToolCardDraft) {
        return checkTurn();
    }

    @Override
    public Boolean checkUsability(ButtonDraftPool buttonDraftPool, StateWindowEnd stateWindowEnd) {
        return false;
    }

    @Override
    public Boolean checkUsability(ButtonDraftPool buttonDraftPool, StateWindowPlace stateWindowPlace) {
        return false;
    }

    @Override
    public Boolean checkUsability(ButtonDraftPool buttonDraftPool, StateWindowStart stateWindowStart) {
        return false;
    }

    @Override
    public Boolean checkUsability(ButtonDraftPool buttonDraftPool, StateRoundTracker stateRoundTracker) {
        return false;
    }

    @Override
    public Boolean checkUsability(ButtonDraftPool buttonDraftPool, StateTurn stateTurn) {
        return (checkTurn() && (!gameSceneController.getModelView().hasDraftedDie()));
    }

    @Override
    public Boolean checkUsability(ButtonToolCard buttonToolCard, StateToolCardDraft stateToolCardDraft) {
        return checkTurn();
    }

    @Override
    public Boolean checkUsability(ButtonToolCard buttonToolCard, StateWindowEnd stateWindowEnd) {
        return checkTurn();
    }

    @Override
    public Boolean checkUsability(ButtonToolCard buttonToolCard, StateWindowPlace stateWindowPlace) {
        return checkTurn();
    }

    @Override
    public Boolean checkUsability(ButtonToolCard buttonToolCard, StateWindowStart stateWindowStart) {
        return checkTurn();
    }

    @Override
    public Boolean checkUsability(ButtonToolCard buttonToolCard, StateRoundTracker stateRoundTracker) {
        return checkTurn();
    }

    @Override
    public Boolean checkUsability(ButtonToolCard buttonToolCard, StateTurn stateTurn) {
        return (checkTurn() && (gameSceneController.getModelView().getToolCardUsability().get(buttonToolCard.getNumberOfToolCard())));
    }


    //TODO capire se funziona questo visitor. Ha oltre al modelli che usiamo solitamente anche lo state come parametro. Non passeremo mai State, ma solo figli
    //Quindi questo metodo non dovrebbe mai essere eseguito
    @Override
    public Boolean checkUsability(ButtonDraftPool buttonDraftPool, State currentState) {
        return null;
    }

    @Override
    public Boolean checkUsability(ButtonPass buttonPass, State currentState) {
        return checkTurn();
    }

    //TODO stesso di prima
    @Override
    public Boolean checkUsability(ButtonSquare buttonSquare, State currentState) {
        return null;
    }

    //TODO come sopra
    @Override
    public Boolean checkUsability(ButtonToolCard buttonToolCard, State currentState) {
        return null;
    }
}
