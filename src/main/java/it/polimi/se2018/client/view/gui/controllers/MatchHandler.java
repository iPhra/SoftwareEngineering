package it.polimi.se2018.client.view.gui.controllers;

import it.polimi.se2018.client.GUIClient;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MatchHandler {
    GUIClient guiClient;
    Stage stage;
    List<String> sortedPlayersNames;
    List<Integer> sortedPlayersScores;
    boolean isLastPlayer;
    boolean toScoreBoardScene;

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
            stage.setMinWidth(1000);
            stage.setMinHeight(667);
            stage.setWidth(1000);
            stage.setHeight(667);
            setConnectionSceneController.setStage(stage);
            scene.setRoot(root);
        } catch(IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE,"MatchHandler",e);
        }
    }

    /**
     * This method is called (togheter with setSortedPlayersScores) when ScoreBoardResponse is received
     * @param sortedPlayersNames it's the list of players in order
     */
    public void setSortedPlayersNames(List<String> sortedPlayersNames) {
        this.sortedPlayersNames = sortedPlayersNames;
    }

    /**
     * This method is called (togheter with setSortedPlayersNames) when ScoreBoardResponse is received
     * @param sortedPlayersScores it's the list of scores in order
     */
    public void setSortedPlayersScores(List<Integer> sortedPlayersScores) {
        this.sortedPlayersScores = sortedPlayersScores;
    }

    public void setIsLastPlayer(boolean isLastPlayer){
        this.isLastPlayer = isLastPlayer;
    }

    public void setGameEnd() {
        toScoreBoardScene = true;
    }
}
