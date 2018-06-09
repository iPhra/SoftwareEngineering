package it.polimi.se2018.client.view.gui.stategui.statedraftpool;

import it.polimi.se2018.client.view.gui.GameSceneController;
import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityDraftPool;
import it.polimi.se2018.client.view.gui.stategui.State;
import it.polimi.se2018.client.view.gui.stategui.StateTurn;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;

public class StateToolCardDraft extends StateDraftPool {

    public StateToolCardDraft(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
        buttonCheckUsabilityHandler = new ButtonCheckUsabilityDraftPool(gameSceneController);
    }

    @Override
    public void doActionDraftPool(int draftPoolPosition) {
        ToolCardMessage toolCardMessage = gameSceneController.getToolCardMessage();
        toolCardMessage.addDraftPoolPosition(draftPoolPosition);
        if (nextState.isEmpty()){
            gameSceneController.sendToolCardMessage();
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

    @Override
    public void doActionWindow(Coordinate coordinate) {
        //Do not do anything
    }

    @Override
    public void doActionToolCard(int toolCardIndex) {
        if (toolCardIndex == gameSceneController.getToolCardMessage().getToolCardNumber()) {
            gameSceneController.setToolCardMessage(null);
            changeState(new StateTurn(gameSceneController));
            gameSceneController.setAllButton();
        }
    }
}
