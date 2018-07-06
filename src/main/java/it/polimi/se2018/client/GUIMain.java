package it.polimi.se2018.client;

import it.polimi.se2018.client.view.gui.controllers.SetConnectionSceneController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIMain extends Application {
    private final GUIClient guiClient;

    public GUIMain(){
        guiClient = new GUIClient();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/setConnectionScene.fxml")));
        Parent root = loader.load();
        SetConnectionSceneController setConnectionSceneController = loader.getController();
        setConnectionSceneController.setGuiClient(guiClient);
        primaryStage.setTitle("Sagrada Online");
        primaryStage.setScene(new Scene(root, 1000, 667));
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(667);
        setConnectionSceneController.setStage(primaryStage);
        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
    }







    public static void main(String[] args) {
        new GUIMain();
        launch(args);
    }
}
