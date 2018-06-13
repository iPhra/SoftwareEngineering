package it.polimi.se2018.client.view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SetConnectionSceneController {
    private boolean ipGiven;
    private boolean portGiven;

    public SetConnectionSceneController() {
        ipGiven = false;
        portGiven = false;
    }

    @FXML
    private TextField ipAddressField;

    @FXML
    private TextField portField;

    @FXML
    private Button defaultSettingsButton;

    //needs further implementation
    public void OnReturnHandler(){
        String ip = ipAddressField.getText();
        int port = Integer.parseInt(portField.getText());
    }

    //needs implementation
    public void defaultSettingsChosen(){

    }
}
