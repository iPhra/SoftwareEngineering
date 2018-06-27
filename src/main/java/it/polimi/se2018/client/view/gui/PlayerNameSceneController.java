package it.polimi.se2018.client.view.gui;

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
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerNameSceneController implements SceneController {
    private GUIClient guiClient;
    private Stage stage;
    private List<Window> windows;
    private PrivateObjective privateObjective;

    private boolean reconnecting;

    @FXML
    private TextField nickTextField;

    @FXML
    private Label label;

    private void toWindowScene(Scene scene) {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/selectWindowScene.fxml")));
        try {
            SelectWindowSceneController selectWindowSceneController = new SelectWindowSceneController(windows, privateObjective, ((GUIView) guiClient.getGUIView()).getGuiController());
            selectWindowSceneController.setGuiClient(guiClient);
            loader.setController(selectWindowSceneController);
            Parent root = loader.load();
            ((GUIView) guiClient.getGUIView()).getGuiController().setSceneController(selectWindowSceneController);
            stage.setWidth(1000);
            stage.setHeight(700);
            selectWindowSceneController.setStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toMatchScene(Scene scene) {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/GameScene.fxml")));
        try {
            GameSceneController gameSceneController = new GameSceneController(((GUIView)(guiClient.getGUIView())).getGuiController());
            gameSceneController.setClientGUI(guiClient);
            loader.setController(gameSceneController);
            Parent root = loader.load();
            ((GUIView)(guiClient.getGUIView())).getGuiController().setSceneController(gameSceneController);
            stage.setWidth(1440);
            stage.setHeight(900);
            gameSceneController.setStage(stage);
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

    public void onReturnHandler(){
        if (!guiClient.setPlayerName(nickTextField.getText())) {
            ((GUIView) guiClient.getGUIView()).getGuiController().setSceneController(this);
            label.setText("Your nickname is ok. Waiting for other players");
            nickTextField.setEditable(false);
        } else label.setText("Nickname already taken, choose another one");
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

    @Override
    public Scene getScene(){
        return nickTextField.getScene();
    }

    @Override
    public void changeScene(Scene scene) {
        if(reconnecting) toMatchScene(scene);
        else toWindowScene(scene);
    }
}


