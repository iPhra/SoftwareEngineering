package it.polimi.se2018.client.view.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * This is the controller of the scene that is used when the player has to decide if increase or decrease the value of the die in his hand
 * During the usage of {@link it.polimi.se2018.mvc.model.toolcards.GrozingPliers}
 */
public class ChoosePlusOrMinusSceneController{
    private GameSceneController gameSceneController;

    /**
     * This is the button that the player clicks if he wants "+1"
     */
    @FXML
    private Button plusOneButton;

    /**
     * This is the button that the player clicks if he wants "-1"
     */
    @FXML
    private Button minusOneButton;

    public void setGameSceneController(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    /**
     * This is the button that the player clicks if he wants "+1"
     */
    public void plusOne(){
        gameSceneController.getToolCardMessage().setCondition(true);
        gameSceneController.sendToolCardMessage();
        Stage stage = (Stage)plusOneButton.getScene().getWindow();
        stage.close();
    }

    /**
     * This is the button that the player clicks if he wants "-1"
     */
    public void minusOne(){
        gameSceneController.getToolCardMessage().setCondition(false);
        gameSceneController.sendToolCardMessage();
        Stage stage = (Stage)minusOneButton.getScene().getWindow();
        stage.close();
    }
}
