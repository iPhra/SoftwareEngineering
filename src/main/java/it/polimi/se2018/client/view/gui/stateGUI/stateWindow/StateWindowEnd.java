package it.polimi.se2018.client.view.gui.stateGUI.stateWindow;

import it.polimi.se2018.client.view.gui.GameSceneController;
import it.polimi.se2018.client.view.gui.stateGUI.State;
import it.polimi.se2018.client.view.gui.stateGUI.StateTurn;
import it.polimi.se2018.network.messages.Coordinate;
import javafx.scene.Scene;

import java.util.List;

public class StateWindowEnd extends StateWindow {
    public StateWindowEnd(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    @Override
    public void doActionWindow(Coordinate coordinate) {
        gameSceneController.getToolCardMessage().addFinalPosition(coordinate);
        if (nextState.isEmpty()){
            //TODO game manager send ToolCardMessage
            changeState(new StateTurn(gameSceneController));
            gameSceneController.disableAllButton();
        }
        else {
            State state = nextState.get(0);
            nextState.remove(0);
            state.setNextState(nextState);
            gameSceneController.setCurrentState(state);
        }
    }
}
