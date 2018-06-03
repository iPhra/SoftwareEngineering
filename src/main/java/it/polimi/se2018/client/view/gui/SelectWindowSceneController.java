package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.mvc.model.Window;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SelectWindowSceneController implements SceneController, Initializable{
    private GUIClientView guiClientView;
    private List<Window> windows;
    private Stage stage;


    @FXML
    private GridPane gridPane;

    SelectWindowSceneController(List<Window> windows, GUIClientView guiClientView){
        this.windows = windows;
        this.guiClientView = guiClientView;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Button[] button = new Button[4];
        for (int i=0;i<4; i++){
            FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/windowImageScene.fxml")));
            loader.setController(new WindowImageSceneController(windows.get(i)));
            try {
                gridPane.add(loader.load(),i,0);
                button[i] = new Button(windows.get(i).getTitle());
                gridPane.add(button[i],i,1);
            } catch (IOException e) {
                Logger logger = Logger.getAnonymousLogger();
                logger.log(Level.ALL,e.getMessage());
            }
        }
        button[0].setOnAction( e -> guiClientView.setWindowNumber(0));
        button[1].setOnAction( e -> guiClientView.setWindowNumber(1));
        button[2].setOnAction( e -> guiClientView.setWindowNumber(2));
        button[3].setOnAction( e -> guiClientView.setWindowNumber(3));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void changeScene(Scene scene) {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/GameScene.fxml")));
        try {
            GameSceneController gameSceneController = new GameSceneController(guiClientView,guiClientView.getPlayerID());
            loader.setController(gameSceneController);
            Parent root = loader.load();
            guiClientView.setSceneController(gameSceneController);
            gameSceneController.setStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

    @Override
    public Scene getScene() {
        return gridPane.getScene();
    }
}
