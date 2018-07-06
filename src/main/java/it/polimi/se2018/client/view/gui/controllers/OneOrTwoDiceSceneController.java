package it.polimi.se2018.client.view.gui.controllers;

import it.polimi.se2018.client.view.gui.stategui.State;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateRoundTracker;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateWindowEnd;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateWindowStart;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the controller of the scene that is used to ask to the player if he would move
 * one or two dice during the usage of {@link it.polimi.se2018.mvc.model.toolcards.TapWheel}
 */
public class OneOrTwoDiceSceneController {
    /**
     * This is the scene of the game
     */
    private GameSceneController gameSceneController;

    /**
     * This is the button that the player clicks if he wants "one"
     */
    @FXML
    private Button buttonOne;

    /**
     * This is the button that the player clicks if he wants "two"
     */
    @FXML
    private Button buttonTwo;

    public void setGameSceneController(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    /**
     * This method is called when buttonOne is clicked and sends to the server the choice of the player
     */
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

    /**
     * This method is called when buttonTwo is clicked and sends to the server the choice of the player
     */
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
