package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.GUIClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ScoreBoardSceneController implements Initializable, SceneController{
    private final List<String> sortedPlayersNames;
    private final List<Integer> sortedPlayersScores;
    private GUIClient guiClient;
    private Stage stage;

    @FXML
    private Label scoreboardLabel;

    @FXML
    private Button newGameButton;

    @FXML
    private BorderPane borderPane;

    ScoreBoardSceneController(List<String> sortedPlayersNames, List<Integer> sortedPlayersScores){
        this.sortedPlayersNames = sortedPlayersNames;
        this.sortedPlayersScores = sortedPlayersScores;
    }

    public void startNewGame() {
        guiClient.setGameEnded();
        changeScene(getScene());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        borderPane.setPadding(new Insets(0,0,50,0));
        StringBuilder builder = new StringBuilder();
        for(int i=0; i< sortedPlayersNames.size(); i++){
            String tmp = i+1 + "  Player: " + sortedPlayersNames.get(i) + "     Score: " + sortedPlayersScores.get(i)+"\n";
            builder.append(tmp);
        }
        scoreboardLabel.setText(builder.toString());
        newGameButton.setOnAction(e -> startNewGame());
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
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/setConnectionScene.fxml")));
        try {
            Parent root = loader.load();
            SetConnectionSceneController setConnectionSceneController = loader.getController();
            setConnectionSceneController.setGuiClient(guiClient);
            stage.setWidth(1000);
            stage.setHeight(667);
            setConnectionSceneController.setStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
