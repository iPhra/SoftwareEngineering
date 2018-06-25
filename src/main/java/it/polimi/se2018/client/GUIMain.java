package it.polimi.se2018.client;

import it.polimi.se2018.client.view.gui.SetConnectionSceneController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GUIMain extends Application {
    private final GUIClient guiClient;

    public GUIMain(){
        guiClient = new GUIClient();
    }

    private GUIClient getGuiClient() {
        return guiClient;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/setConnectionScene.fxml")));
        Parent root = loader.load();
        SetConnectionSceneController setConnectionSceneController = loader.getController();
        setConnectionSceneController.setGuiClient(guiClient);
        primaryStage.setTitle("Sagrada Online");
        primaryStage.setScene(new Scene(root, 1000, 667));
        setConnectionSceneController.setStage(primaryStage);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        primaryStage.show();
    }









    public static void main(String[] args) {
        GUIMain guiMain = new GUIMain();
        new Thread(guiMain.getGuiClient()).start();
        launch(args);
    }
}
