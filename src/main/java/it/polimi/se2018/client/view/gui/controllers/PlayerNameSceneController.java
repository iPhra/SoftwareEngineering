package it.polimi.se2018.client.view.gui.controllers;

import it.polimi.se2018.client.GUIClient;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.PrivateObjective;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class PlayerNameSceneController implements SceneController {
    private GUIClient guiClient;
    private Stage stage;
    private List<Window> windows;
    private PrivateObjective privateObjective;
    private boolean windowSelectionOver;
    private boolean reconnecting;

    @FXML
    private TextField nickTextField;

    @FXML
    private Label label;

    private void toWindowScene(Scene scene) { //called normally when not reconnecting
        windowSelectionOver = false;
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/selectWindowScene.fxml")));
        try {
            SelectWindowSceneController selectWindowSceneController = new SelectWindowSceneController(windows, privateObjective, (guiClient.getGUIView()).getGuiLogic());
            selectWindowSceneController.setClientGUI(guiClient);
            loader.setController(selectWindowSceneController);
            Parent root = loader.load();
            guiClient.getGUIView().getGuiLogic().setSceneController(selectWindowSceneController);
            stage.setWidth(1000);
            stage.setHeight(700);
            selectWindowSceneController.setStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toMatchScene(Scene scene) { //called if i'm reconnecting when the game is already after window selection
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/GameScene.fxml")));
        try {
            GameSceneController gameSceneController = new GameSceneController(guiClient.getGUIView().getGuiLogic());
            gameSceneController.setClientGUI(guiClient);
            loader.setController(gameSceneController);
            Parent root = loader.load();
            guiClient.getGUIView().getGuiLogic().setSceneController(gameSceneController);
            stage.setWidth(1440);
            stage.setHeight(900);
            gameSceneController.setStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toReconnectionScene(Scene scene) { //called when reconnecting during window selecton
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/reconnectionScene.fxml")));
        try {
            Parent root = loader.load();
            ReconnectionSceneController reconnectionSceneController = loader.getController();
            reconnectionSceneController.setClientGUI(guiClient);
            loader.setController(reconnectionSceneController);
            guiClient.getGUIView().getGuiLogic().setSceneController(reconnectionSceneController);
            stage.setWidth(600);
            stage.setHeight(623);
            reconnectionSceneController.setStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setWindows(List<Window> windows) {
        this.windows = windows;
    }

    public void setPrivateObjective(PrivateObjective privateObjective) {
        this.privateObjective = privateObjective;
    }

    public void setGuiClient(GUIClient guiClient) {
        this.guiClient = guiClient;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setReconnecting() {
        reconnecting = true;
    }

    public void setWindowSelectionOver() {
        windowSelectionOver = true;
    }

    public void onReturnHandler(){
        if (!guiClient.setPlayerName(nickTextField.getText())) {
            guiClient.getGUIView().getGuiLogic().setSceneController(this);
            label.setText("Your nickname is ok. Waiting for other players");
            nickTextField.setEditable(false);
        } else label.setText("Nickname already taken, choose another one");
    }

    @Override
    public Scene getScene(){
        return nickTextField.getScene();
    }

    @Override
    public void changeScene(Scene scene) {
        if(!reconnecting) toWindowScene(scene);
        else if (windowSelectionOver) toMatchScene(scene);
        else toReconnectionScene(scene);
    }
}


