package it.polimi.se2018.client.view.gui.stategui;

import it.polimi.se2018.client.view.gui.GameSceneController;
import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityHandler;
import it.polimi.se2018.network.messages.Coordinate;

import java.util.List;

public abstract class State {
    protected GameSceneController gameSceneController;
    protected List<State> nextState;
    protected ButtonCheckUsabilityHandler buttonCheckUsabilityHandler;

    public void setNextState(List<State> nextState) {
        this.nextState = nextState;
    }

    public void changeState(State state){
        gameSceneController.setCurrentState(state);
        gameSceneController.setAllButton();
    }

    public ButtonCheckUsabilityHandler getButtonCheckUsabilityHandler() {
        return buttonCheckUsabilityHandler;
    }

    public abstract void doActionWindow(Coordinate coordinate);

    public abstract void doActionDraftPool(int draftPoolPosition);

    public abstract void doActionToolCard(int toolCardIndex);
}
