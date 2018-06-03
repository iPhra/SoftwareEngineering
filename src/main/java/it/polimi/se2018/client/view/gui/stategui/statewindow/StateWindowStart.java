package it.polimi.se2018.client.view.gui.stategui.statewindow;

import it.polimi.se2018.client.view.gui.GameSceneController;
import it.polimi.se2018.client.view.gui.stategui.State;
import it.polimi.se2018.network.messages.Coordinate;

public class StateWindowStart extends StateWindow {

    public StateWindowStart(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    @Override
    public void doActionWindow(Coordinate coordinate) {
        gameSceneController.getToolCardMessage().addStartingPosition(coordinate);
        State state = nextState.get(0);
        nextState.remove(0);
        state.setNextState(nextState);
        gameSceneController.setCurrentState(state);
    }
}