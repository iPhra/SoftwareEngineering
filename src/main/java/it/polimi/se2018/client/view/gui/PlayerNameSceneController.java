package it.polimi.se2018.client.view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerNameSceneController implements SceneController{
    private ClientGUI clientGUI;
    @FXML
    private TextField nickTextField;

    public void onReturnHandler(){
        if (clientGUI.isSocket()){
            if (clientGUI.getPlayerNameSocket(nickTextField.getText())){
                changeScene(nickTextField);
            }
            //here i need the else that prints that nick is not ok
        }
        //here i need the else for the RMI connection

    }

    @Override
    public void setClientGUI(ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
    }

    private void changeScene(TextField textField) {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/selectWindowScene.fxml")));
        try {
            Parent root = loader.load();
            SceneController sceneController = loader.getController();
            sceneController.setClientGUI(clientGUI);
            ((GUIClientView) clientGUI.getClientView()).setSceneController(sceneController);
            textField.getScene().setRoot(root);
        } catch (IOException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }
}


