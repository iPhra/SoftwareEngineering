package it.polimi.se2018.client.view.gui.stateGUI;

import it.polimi.se2018.client.view.gui.GameSceneController;

import java.util.List;

public abstract class State {
    public GameSceneController gameSceneController;
    public List<State> nextState;

    public void setNextState(List<State> nextState) {
        this.nextState = nextState;
    }

    public void changeState(State state){
        gameSceneController.setCurrentState(state);
        gameSceneController.setAllButton();
    }

    void setButton(GameSceneController gameSceneController){};
}
