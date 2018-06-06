package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.GUIClient;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.utils.WindowBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<String> sortedPlayersNames = Arrays.asList("Emilio", "Cristiano", "Francesco Lorenzo");
        List<Integer> sortedPlayersScores = Arrays.asList(69,42,7);
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/scoreBoardScene.fxml")));
        ScoreBoardSceneController scoreBoardSceneController = new ScoreBoardSceneController(sortedPlayersNames,sortedPlayersScores);
        scoreBoardSceneController.setGUIClient(new GUIClient());
        scoreBoardSceneController.setStage(primaryStage);
        loader.setController(scoreBoardSceneController);
        Parent root = loader.load();
        primaryStage.setTitle("Sagrada Online");
        primaryStage.setScene(new Scene(root, 600, 623));
        primaryStage.show();

    }
}
