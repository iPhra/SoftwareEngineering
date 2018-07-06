package it.polimi.se2018.client.view.gui.stategui.statewindow;

import it.polimi.se2018.client.view.gui.controllers.GameSceneController;
import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityWindow;
import it.polimi.se2018.client.view.gui.stategui.State;
import it.polimi.se2018.client.view.gui.stategui.StateTurn;
import it.polimi.se2018.network.messages.Coordinate;
import javafx.application.Platform;

/**
 * This state is reach during the usage of toolcard that move die, when the player has to select a final
 * position of a die
 */
public class StateWindowEnd extends StateWindow {
    public StateWindowEnd(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
        buttonCheckUsabilityHandler = new ButtonCheckUsabilityWindow(gameSceneController);
        descriptionOfState = "Select the final posizion of the selected die";
    }

    @Override
    public void doActionWindow(Coordinate coordinate) {
        gameSceneController.getToolCardMessage().addFinalPosition(coordinate);
        if (nextState.isEmpty()){
            Platform.runLater(() -> {
                changeState(new StateTurn(gameSceneController));
                gameSceneController.disableAllButton();
            });
            gameSceneController.sendToolCardMessage();
        }
        else {
            State state = nextState.get(0);
            nextState.remove(0);
            state.setNextState(nextState);
            Platform.runLater(() -> changeState(state));
        }
    }
}
