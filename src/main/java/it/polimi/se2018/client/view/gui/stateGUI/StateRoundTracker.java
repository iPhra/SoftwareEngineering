package it.polimi.se2018.client.view.gui.stateGUI;

import it.polimi.se2018.client.view.gui.GameSceneController;
import it.polimi.se2018.client.view.gui.stateGUI.stateWindow.StateWindow;
import it.polimi.se2018.network.messages.Coordinate;

import java.util.List;

public class StateRoundTracker extends StateWindow {

    public StateRoundTracker(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    public void doAction(Coordinate coordinate){

    }

    @Override
    public void doActionWindow(Coordinate coordinate) {
        gameSceneController.getToolCardMessage().addRoundTrackerPosition(coordinate);
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
