package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.view.gui.stategui.State;
import it.polimi.se2018.client.view.gui.stategui.StateRoundTracker;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateWindowEnd;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateWindowStart;
import it.polimi.se2018.network.messages.requests.InputMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class AnotherDieYesOrNoSceneController {
    private GameSceneController gameSceneController;

    @FXML
    private Button buttonOne;

    @FXML
    private Button buttonTwo;

    public void setGameSceneController(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    public void buttonOneClicked(){
        gameSceneController.getToolCardMessage().setCondition(false);
        StateWindowStart stateWindowStart = new StateWindowStart(gameSceneController);
        StateWindowEnd stateWindowEnd = new StateWindowEnd(gameSceneController);
        StateRoundTracker stateRoundTracker = new StateRoundTracker(gameSceneController);
        List<State> states = new ArrayList<>();
        states.add(stateWindowEnd);
        states.add(stateRoundTracker);
        stateWindowStart.setNextState(states);
        gameSceneController.getCurrentState().changeState(stateWindowStart);
        gameSceneController.setAllButton();
        Stage stage = (Stage)buttonTwo.getScene().getWindow();
        stage.close();
    }

    public void buttonTwoClicked(){
        gameSceneController.getToolCardMessage().setCondition(true);
        StateWindowStart stateWindowStart = new StateWindowStart(gameSceneController);
        StateWindowEnd stateWindowEnd = new StateWindowEnd(gameSceneController);
        StateWindowStart stateWindowStart2 = new StateWindowStart(gameSceneController);
        StateWindowEnd stateWindowEnd2 = new StateWindowEnd(gameSceneController);
        StateRoundTracker stateRoundTracker = new StateRoundTracker(gameSceneController);
        List<State> states = new ArrayList<>();
        states.add(stateWindowEnd);
        states.add(stateWindowStart2);
        states.add(stateWindowEnd2);
        states.add(stateRoundTracker);
        stateWindowStart.setNextState(states);
        gameSceneController.getCurrentState().changeState(stateWindowStart);
        gameSceneController.setAllButton();
        Stage stage = (Stage)buttonOne.getScene().getWindow();
        stage.close();
    }
}
