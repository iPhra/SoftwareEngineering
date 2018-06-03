package it.polimi.se2018.client.view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StartingSceneController implements SceneController {
    private ClientGUI clientGUI;
    private Stage stage;
    @FXML
    private Button socketButton;
    @FXML
    private Button rmiButton;

    public void socketButtonClicked(){
        clientGUI.createSocketConnection();
        changeScene(getScene());
    }

    public void rmiButtonClicked(){
        clientGUI.createRMIConnection();
        changeScene(getScene());
    }

    public void setClientGUI(ClientGUI clientGUI){
        this.clientGUI = clientGUI;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Scene getScene(){
        return socketButton.getScene();
    }

    @Override
    public void changeScene(Scene scene){
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/playerNameScene.fxml")));
        try{
            Parent root = loader.load();
            PlayerNameSceneController playerNameSceneController = loader.getController();
            playerNameSceneController.setClientGUI(clientGUI);
            playerNameSceneController.setStage(stage);
            scene.setRoot(root);
        }catch(IOException e){
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }
}
