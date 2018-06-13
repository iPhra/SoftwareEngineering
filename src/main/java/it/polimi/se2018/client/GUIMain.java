package it.polimi.se2018.client;

import it.polimi.se2018.client.view.gui.SetConnectionSceneController;
import it.polimi.se2018.client.view.gui.StartingSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIMain extends Application {
    private static GUIClient guiClient;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/setConnectionScene.fxml")));
        Parent root = loader.load();
        SetConnectionSceneController setConnectionSceneController = loader.getController();
        setConnectionSceneController.setGuiClient(guiClient);
        primaryStage.setTitle("Sagrada Online");
        primaryStage.setScene(new Scene(root, 1000, 667));
        setConnectionSceneController.setStage(primaryStage);
        primaryStage.show();
    }









    public static void main(String[] args) {
        GUIMain.guiClient = new GUIClient();
        new Thread(guiClient).start();
        launch(args);
    }
}
