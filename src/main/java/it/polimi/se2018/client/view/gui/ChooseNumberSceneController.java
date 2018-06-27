package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.network.messages.requests.InputMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ChooseNumberSceneController {
    private GameSceneController gameSceneController;

    @FXML
    private Button button1;

    @FXML
    private Button button2;

    @FXML
    private Button button3;

    @FXML
    private Button button4;

    @FXML
    private Button button5;

    @FXML
    private Button button6;

    public void setGameSceneController(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    public void button1Clicked(){
        //TODO gameSceneController.getGuiView().handleNetworkOutput(?);
        gameSceneController.getGuiView().handleNetworkOutput(new InputMessage(gameSceneController.getPlayerID(), gameSceneController.getGuiModel().getBoard().getStateID(), 1));
        Stage stage = (Stage)button1.getScene().getWindow();
        stage.close();
    }

    public void button2Clicked(){
        //TODO gameSceneController.getGuiView().handleNetworkOutput(?);
        gameSceneController.getGuiView().handleNetworkOutput(new InputMessage(gameSceneController.getPlayerID(), gameSceneController.getGuiModel().getBoard().getStateID(), 1));
        Stage stage = (Stage)button1.getScene().getWindow();
        stage.close();
    }

    public void button3Clicked(){
        //TODO gameSceneController.getGuiView().handleNetworkOutput(?);
        gameSceneController.getGuiView().handleNetworkOutput(new InputMessage(gameSceneController.getPlayerID(), gameSceneController.getGuiModel().getBoard().getStateID(), 1));
        Stage stage = (Stage)button1.getScene().getWindow();
        stage.close();
    }

    public void button4Clicked(){
        //TODO gameSceneController.getGuiView().handleNetworkOutput(?);
        gameSceneController.getGuiView().handleNetworkOutput(new InputMessage(gameSceneController.getPlayerID(), gameSceneController.getGuiModel().getBoard().getStateID(), 1));
        Stage stage = (Stage)button1.getScene().getWindow();
        stage.close();
    }

    public void button5Clicked(){
        //TODO gameSceneController.getGuiView().handleNetworkOutput(?);
        gameSceneController.getGuiView().handleNetworkOutput(new InputMessage(gameSceneController.getPlayerID(), gameSceneController.getGuiModel().getBoard().getStateID(), 1));
        Stage stage = (Stage)button1.getScene().getWindow();
        stage.close();
    }

    public void button6Clicked(){
        //TODO gameSceneController.getGuiView().handleNetworkOutput(?);
        gameSceneController.getGuiView().handleNetworkOutput(new InputMessage(gameSceneController.getPlayerID(), gameSceneController.getGuiModel().getBoard().getStateID(), 1));
        Stage stage = (Stage)button1.getScene().getWindow();
        stage.close();
    }
}
