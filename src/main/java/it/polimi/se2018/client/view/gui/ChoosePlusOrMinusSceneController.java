package it.polimi.se2018.client.view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ChoosePlusOrMinusSceneController{
    private GameSceneController gameSceneController;

    @FXML
    private Button plusOneButton;

    @FXML
    private Button minusOneButton;

    public void setGameSceneController(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    public void plusOne(){
        gameSceneController.getToolCardMessage().setCondition(true);
        gameSceneController.sendToolCardMessage();
        Stage stage = (Stage)plusOneButton.getScene().getWindow();
        stage.close();
    }

    public void minusOne(){
        gameSceneController.getToolCardMessage().setCondition(false);
        gameSceneController.sendToolCardMessage();
        Stage stage = (Stage)minusOneButton.getScene().getWindow();
        stage.close();
    }
}
