package it.polimi.se2018.client.view.gui.stategui.statewindow;

import it.polimi.se2018.client.view.gui.controllers.GameSceneController;
import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityWindow;
import it.polimi.se2018.client.view.gui.stategui.State;
import it.polimi.se2018.network.messages.Coordinate;
import javafx.application.Platform;

/**
 * This state is reach during the usage of toolcard that move die, when the player has to select a die from his winodw
 */
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
        Platform.runLater(() -> changeState(state));
    }
}