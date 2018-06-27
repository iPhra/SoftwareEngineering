package it.polimi.se2018.client.view.gui.stategui.statewindow;

import it.polimi.se2018.client.view.gui.GameSceneController;
import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityWindow;
import it.polimi.se2018.client.view.gui.stategui.State;
import it.polimi.se2018.client.view.gui.stategui.StateTurn;
import it.polimi.se2018.network.messages.Coordinate;

public class StateWindowStart extends StateWindow {

    public StateWindowStart(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
        buttonCheckUsabilityHandler = new ButtonCheckUsabilityWindow(gameSceneController);
        descriptionOfState = "Select the die from your window";
    }

    @Override
    public void doActionWindow(Coordinate coordinate) {
        gameSceneController.getToolCardMessage().addStartingPosition(coordinate);
        State state = nextState.get(0);
        nextState.remove(0);
        state.setNextState(nextState);
        changeState(state);
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