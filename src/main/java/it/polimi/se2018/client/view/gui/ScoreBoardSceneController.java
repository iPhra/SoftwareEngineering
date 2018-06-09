package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.GUIClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScoreBoardSceneController implements Initializable, SceneController{
    private final List<String> sortedPlayersNames;
    private final List<Integer> sortedPlayersScores;
    private GUIClient guiClient;
    private Stage stage;

    @FXML
    private Label scoreboardLabel;

    @FXML
    private Button newGameButton;

    ScoreBoardSceneController(List<String> sortedPlayersNames, List<Integer> sortedPlayersScores){
        this.sortedPlayersNames = sortedPlayersNames;
        this.sortedPlayersScores = sortedPlayersScores;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        StringBuilder builder = new StringBuilder();
        for(int i=0; i< sortedPlayersNames.size(); i++){
            String tmp = i+1 + "  Player: " + sortedPlayersNames.get(i) + "     Score: " + sortedPlayersScores.get(i)+"\n";
            builder.append(tmp);
        }
        scoreboardLabel.setText(builder.toString());
        newGameButton.setOnAction(e -> changeScene(getScene()));
    }

    public void setGuiClient(GUIClient guiClient) {
        this.guiClient = guiClient;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Scene getScene() {
        return scoreboardLabel.getScene();
    }

    @Override
    public void changeScene(Scene scene) {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/startingScene.fxml")));
        try {
            Parent root = loader.load();
            StartingSceneController startingSceneController = loader.getController();
            startingSceneController.setGuiClient(guiClient);
            startingSceneController.setStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }
}