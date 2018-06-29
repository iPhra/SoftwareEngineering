package it.polimi.se2018.client.view.gui.stategui.statewindow;

import it.polimi.se2018.client.view.gui.GameSceneController;
import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityWindow;
import it.polimi.se2018.client.view.gui.stategui.State;
import it.polimi.se2018.client.view.gui.stategui.StateTurn;
import it.polimi.se2018.network.messages.Coordinate;
import javafx.application.Platform;

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
            gameSceneController.sendToolCardMessage();
            Platform.runLater(() -> {
                changeState(new StateTurn(gameSceneController));
                gameSceneController.disableAllButton();
            });
        }
        else {
            State state = nextState.get(0);
            nextState.remove(0);
            state.setNextState(nextState);
            Platform.runLater(() -> changeState(state));
        }
    }

    @Override
    public void doActionDraftPool(int draftPoolPosition) {
        //To do something?
    }

    @Override
    public void doActionToolCard(int toolCardIndex) {
        if (toolCardIndex == gameSceneController.getToolCardMessage().getToolCardNumber()) {
            gameSceneController.setToolCardMessage(null);
            Platform.runLater(() -> {
                changeState(new StateTurn(gameSceneController));
                gameSceneController.setAllButton();
            });
        }
    }
}
