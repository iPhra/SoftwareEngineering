package it.polimi.se2018.client.view.gui.controllers;

import it.polimi.se2018.client.GUIClient;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DisconnectionHandler {
    GUIClient guiClient;
    protected Stage stage;

    public void setClientGUI(GUIClient guiClient) {
        this.guiClient = guiClient;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void returnToSetConnectionScene(Scene scene){
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/setConnectionScene.fxml")));
        try{
            Parent root = loader.load();
            SetConnectionSceneController setConnectionSceneController = loader.getController();
            setConnectionSceneController.setGuiClient(guiClient);
            stage.setWidth(1000);
            stage.setHeight(667);
            setConnectionSceneController.setStage(stage);
            scene.setRoot(root);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
