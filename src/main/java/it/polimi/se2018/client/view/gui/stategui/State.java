package it.polimi.se2018.client.view.gui.stategui;

import it.polimi.se2018.client.view.gui.GameSceneController;

import java.util.List;

public abstract class State {
    protected GameSceneController gameSceneController;
    protected List<State> nextState;

    public void setNextState(List<State> nextState) {
        this.nextState = nextState;
    }

    public void changeState(State state){
        gameSceneController.setCurrentState(state);
        gameSceneController.setAllButton();
    }

    void setButton(GameSceneController gameSceneController){
        //implement (è astratto?)
    }
}
