package it.polimi.se2018.client.view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartingSceneController implements SceneController {
    private ClientGUI clientGUI;
    @FXML
    private Button socketButton;
    @FXML
    private Button rmiButton;

    public void socketButtonClicked(){
        clientGUI.createSocketConnection();
        changeScene(socketButton);
    }

    public void rmiButtonClicked(){
        clientGUI.createRMIConnection();
        changeScene(rmiButton);
    }

    @Override
    public void setClientGUI(ClientGUI clientGUI){
        this.clientGUI = clientGUI;
    }

    private void changeScene(Button button){
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/playerNameScene.fxml")));
        try{
            Parent root = loader.load();
            SceneController sceneController = loader.getController();
            sceneController.setClientGUI(clientGUI);
            button.getScene().setRoot(root);
        }catch(IOException e){
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

}
