package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.utils.WindowBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

/**
 * This Class will be deleted, i use it to test specific scenes
 * @author Emilio Imperiali
 * @deprecated
 */
public class MainTest extends Application{

    /**
     * @deprecated
     * @param args args of main
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @deprecated
     * @param primaryStage the stage
     * @throws Exception an exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        List<Window> windows = WindowBuilder.extractWindows(1);


        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/selectWindowScene.fxml")));
        loader.setController(new SelectWindowSceneController(windows, new GUIClientView(1)));
        Parent root = loader.load();
        primaryStage.setTitle("Sagrada Online");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();

    }
}
