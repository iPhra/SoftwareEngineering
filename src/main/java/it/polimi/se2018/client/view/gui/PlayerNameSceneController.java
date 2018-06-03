package it.polimi.se2018.client.view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerNameSceneController implements SceneController{
    private ClientGUI clientGUI;
    @FXML
    private TextField nickTextField;

    @FXML
    private Label label;

    public void onReturnHandler(){
        if (clientGUI.isSocket() && !clientGUI.getPlayerNameSocket(nickTextField.getText())) {
            ((GUIClientView) clientGUI.getGUIClientView()).setSceneController(this);
            label.setText("Your nickname is ok. Waiting for other players");
            //here i need the else that prints that nick is not ok
        }
        //here i need the else for the RMI connection
    }

    public void setClientGUI(ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
    }

    @Override
    public Scene getScene(){
        return nickTextField.getScene();
    }

    @Override
    public void changeScene(Scene scene) {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/selectWindowScene.fxml")));
        try {
            Parent root = loader.load();
            SelectWindowSceneController selectWindowSceneController = loader.getController();
            selectWindowSceneController.setClientGUI(clientGUI);
            ((GUIClientView) clientGUI.getGUIClientView()).setSceneController(selectWindowSceneController);
            scene.setRoot(root);
        } catch (IOException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }
}


