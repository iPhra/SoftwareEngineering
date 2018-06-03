package it.polimi.se2018.client.view.gui.stategui.statedraftpool;

import it.polimi.se2018.client.view.gui.GameSceneController;
import it.polimi.se2018.client.view.gui.stategui.State;
import it.polimi.se2018.client.view.gui.stategui.StateTurn;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;

public class StateToolCardDraft extends StateDraftPool {

    public StateToolCardDraft(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    @Override
    public void doAction(int draftPoolPosition) {
        ToolCardMessage toolCardMessage = (ToolCardMessage) gameSceneController.getMessage();
        toolCardMessage.addDraftPoolPosition(draftPoolPosition);
        if (nextState.isEmpty()){
            //game manager send ToolCardMessage
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
