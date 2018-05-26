package it.polimi.se2018.client.view.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import java.io.IOException;

public class StartingSceneController implements SceneController {
    private ClientGUI clientGUI;
    @FXML
    private Button socketButton;
    @FXML
    private Button RMIButton;

    public void socketButtonClicked(){
        clientGUI.createSocketConnection();
        changeScene(socketButton);
    }

    public void RMIButtonClicked(){
        clientGUI.createRMIConnection();
        changeScene(RMIButton);
    }

    @Override
    public void setClientGUI(ClientGUI clientGUI){
        this.clientGUI = clientGUI;
    }

    private void changeScene(Button button){
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/playerNameScene.fxml")));
        try{
            Parent root = loader.load();
            SceneController sceneController = loader.getController();
            sceneController.setClientGUI(clientGUI);
            button.getScene().setRoot(root);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
