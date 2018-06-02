package it.polimi.se2018.client.view.gui.stateGUI.stateDraftPool;

import it.polimi.se2018.client.view.gui.GameSceneController;
import it.polimi.se2018.client.view.gui.stateGUI.State;
import it.polimi.se2018.client.view.gui.stateGUI.StateTurn;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;

import java.util.List;

public class StateToolCardDraft extends StateDraftPool {

    public StateToolCardDraft(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    @Override
    public void doAction(int draftPoolPosition) {
        ToolCardMessage toolCardMessage = (ToolCardMessage) gameSceneController.getMessage();
        toolCardMessage.addDraftPoolPosition(draftPoolPosition);
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
