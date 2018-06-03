package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.mvc.model.Window;
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

public class PlayerNameSceneController implements SceneController{
    private ClientGUI clientGUI;
    private Stage stage;
    private List<Window> windows;

    @FXML
    private TextField nickTextField;

    @FXML
    private Label label;

    public void setWindows(List<Window> windows) {
        this.windows = windows;
    }

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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Scene getScene(){
        return nickTextField.getScene();
    }

    @Override
    public void changeScene(Scene scene) {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/selectWindowScene.fxml")));
        try {
            SelectWindowSceneController selectWindowSceneController = new SelectWindowSceneController(windows, (GUIClientView) clientGUI.getGUIClientView());
            loader.setController(selectWindowSceneController);
            Parent root = loader.load();
            ((GUIClientView) clientGUI.getGUIClientView()).setSceneController(selectWindowSceneController);
            stage.setWidth(1000);
            stage.setHeight(700);
            selectWindowSceneController.setStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }
}


