package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.network.messages.requests.InputMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AnotherDieYesOrNoSceneController {
    private GameSceneController gameSceneController;

    @FXML
    private Button buttonYes;

    @FXML
    private Button buttonNo;

    public void setGameSceneController(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    public void buttonYesClicked(){
        //TODO gameSceneController.getGuiView().handleNetworkOutput(?);
        Stage stage = (Stage)buttonYes.getScene().getWindow();
        stage.close();
    }

    public void buttonNoClicked(){
        //TODO gameSceneController.getGuiView().handleNetworkOutput(?);
        Stage stage = (Stage)buttonNo.getScene().getWindow();
        stage.close();
    }
}
