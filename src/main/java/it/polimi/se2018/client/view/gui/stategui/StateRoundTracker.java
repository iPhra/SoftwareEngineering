package it.polimi.se2018.client.view.gui.stategui;

import it.polimi.se2018.client.view.gui.GameSceneController;
import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityRoundTracker;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateWindow;
import it.polimi.se2018.network.messages.Coordinate;

public class StateRoundTracker extends StateWindow {

    public StateRoundTracker(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
        buttonCheckUsabilityHandler = new ButtonCheckUsabilityRoundTracker(gameSceneController);
        descriptionOfState = "Select a die from round tracker";
    }

    @Override
    public void doActionWindow(Coordinate coordinate) {
        gameSceneController.getToolCardMessage().addRoundTrackerPosition(coordinate);
        //if (nextState.isEmpty()){
            gameSceneController.sendToolCardMessage();
            changeState(new StateTurn(gameSceneController));
            gameSceneController.disableAllButton();
        /*}
        else {
            State state = nextState.get(0);
            nextState.remove(0);
            state.setNextState(nextState);
            gameSceneController.setCurrentState(state);
        }
        */
    }

    @Override
    public void doActionDraftPool(int draftPoolPosition) {
        //To do something?
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
