package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.ShadesOfBlueObjective;
import it.polimi.se2018.utils.WindowBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

/**
 * This Class will be deleted, i use it to test specific scenes
 * @author Emilio Imperiali
 * @deprecated
 */
@Deprecated
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
        SelectWindowSceneController selectWindowSceneController = new SelectWindowSceneController(windows,ShadesOfBlueObjective.instance("/objectives/private_objectives/shades_of_blue.png"),new GUIClientView(1));
        loader.setController((selectWindowSceneController));
        Parent root = loader.load();
        primaryStage.setTitle("Sagrada Online");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();






        /*Window window = WindowBuilder.extractWindows(1).get(0);
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/windowImageScene.fxml")));
        WindowImageSceneController windowImageSceneController = new WindowImageSceneController(window);
        loader.setController((windowImageSceneController));
        Parent root = loader.load();
        primaryStage.setTitle("Sagrada Online");
        primaryStage.setScene(new Scene(root, 206, 182));
        primaryStage.show();*/




        /*List<List<Die>> roundTracker = Arrays.asList(Arrays.asList(new Die(2, Color.BLUE), new Die(5,Color.PURPLE)),Arrays.asList(new Die(4,Color.GREEN)));
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/RoundTrackerScene.fxml")));
        RoundTrackerSceneController roundTrackerSceneController = new RoundTrackerSceneController(roundTracker);
        loader.setController(roundTrackerSceneController);
        Parent root = loader.load();
        primaryStage.setTitle("Sagrada Online");
        primaryStage.setScene(new Scene(root, 1892, 396));
        primaryStage.show();*/


        /*List<String> sortedPlayersNames = Arrays.asList("Emilio", "Cristiano", "Francesco Lorenzo");
        List<Integer> sortedPlayersScores = Arrays.asList(69,42,7);
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/scoreBoardScene.fxml")));
        ScoreBoardSceneController scoreBoardSceneController = new ScoreBoardSceneController(sortedPlayersNames,sortedPlayersScores);
        scoreBoardSceneController.setGuiClient(new GUIClient());
        scoreBoardSceneController.setStage(primaryStage);
        loader.setController(scoreBoardSceneController);
        Parent root = loader.load();
        primaryStage.setTitle("Sagrada Online");
        primaryStage.setScene(new Scene(root, 600, 623));
        primaryStage.show(); */

    }
}
