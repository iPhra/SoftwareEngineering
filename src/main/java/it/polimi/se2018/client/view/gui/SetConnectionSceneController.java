package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.GUIClient;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SetConnectionSceneController implements SceneController {
    private final GUIClient guiClient;
    private boolean ipGiven;
    private boolean portGiven;
    private String ip;
    private int port;

    public SetConnectionSceneController(GUIClient guiClient) {
        this.guiClient = guiClient;
        ipGiven = false;
        portGiven = false;
    }

    @FXML
    private TextField ipAddressField;

    @FXML
    private TextField portField;

    @FXML
    private Button defaultSettingsButton;

    public void ipHandler() {
        ip = ipAddressField.getText();
        ipGiven = true;
        if (portGiven) guiClient.setDifferentParams(ip, port);
    }

    public void portHandler() {
        port = Integer.parseInt(portField.getText());
        portGiven = true;
        if (ipGiven) guiClient.setDifferentParams(ip, port);
    }

    public void defaultSettingsChosen() {
        guiClient.setDefaultParams();
    }

    @Override
    public void changeScene(Scene scene) {
    }

    @Override
    public Scene getScene() {
        return null;
    }
}
