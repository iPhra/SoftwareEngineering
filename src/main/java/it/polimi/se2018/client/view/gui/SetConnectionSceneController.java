package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.GUIClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SetConnectionSceneController implements SceneController {
    private GUIClient guiClient;
    private Stage stage;
    private boolean ipGiven;
    private boolean portGiven;
    private String ip;
    private int port;

    @FXML
    private ToggleGroup group;

    @FXML
    private RadioButton socketRadioButton;

    @FXML
    private RadioButton rmiRadioButton;

    @FXML
    private TextField ipAddressField;

    @FXML
    private TextField portField;

    @FXML
    private Button defaultSettingsButton;

    public SetConnectionSceneController() {
        ipGiven = false;
        portGiven = false;
    }

    public void setGuiClient(GUIClient guiClient) {
        this.guiClient = guiClient;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void ipHandler() {
        ip = ipAddressField.getText();
        ipGiven = true;
        if (portGiven){
            setSelectedConnection();
            guiClient.setDifferentParams(ip, port);
            createSelectedConnection();
            changeScene(getScene());
        }
    }

    public void portHandler() {
        port = Integer.parseInt(portField.getText());
        portGiven = true;
        if (ipGiven){
            setSelectedConnection();
            guiClient.setDifferentParams(ip, port);
            createSelectedConnection();
            changeScene(getScene());
        }
    }

    public void defaultSettingsChosen() {
        setSelectedConnection();
        guiClient.setDefaultParams();
        createSelectedConnection();
        changeScene(getScene());
    }

    @Override
    public Scene getScene() {
        return defaultSettingsButton.getScene();
    }

    @Override
    public void changeScene(Scene scene) {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/playerNameScene.fxml")));
        try{
            Parent root = loader.load();
            PlayerNameSceneController playerNameSceneController = loader.getController();
            playerNameSceneController.setGuiClient(guiClient);
            stage.setWidth(600);
            stage.setHeight(623);
            playerNameSceneController.setStage(stage);
            scene.setRoot(root);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void setSelectedConnection(){
        if(group.getSelectedToggle().equals(socketRadioButton))
            guiClient.setSocket(true);
        else
            guiClient.setSocket(false);
    }


    private void createSelectedConnection(){
        if(group.getSelectedToggle().equals(socketRadioButton))
            guiClient.createSocketConnection();
        else
            guiClient.createRMIConnection();
    }


}
