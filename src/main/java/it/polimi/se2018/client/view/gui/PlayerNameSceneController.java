package it.polimi.se2018.client.view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerNameSceneController implements SceneController{
    private ClientGUI clientGUI;
    @FXML
    private TextField nickTextField;

    public void onReturnHandler(){
        System.out.println(nickTextField.getText());
    }

    @Override
    public void setClientGUI(ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
    }

    private void changeScene(Button button) {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/selectWindowScene.fxml")));
        try {
            Parent root = loader.load();
            SceneController sceneController = loader.getController();
            sceneController.setClientGUI(clientGUI);
            button.getScene().setRoot(root);
        } catch (IOException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }
}


