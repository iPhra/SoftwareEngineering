package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.view.gui.button.ButtonSquare;
import it.polimi.se2018.network.messages.Coordinate;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WindowSceneController implements SceneController, Initializable {
    @FXML
    private GridPane gridWindow;

    private List<ButtonSquare> buttonSquares;

    private int playerID;

    public WindowSceneController(int playerID) {
        this.playerID = playerID;
        buttonSquares = new ArrayList<>();
    }

    public List<ButtonSquare> getButtonSquares() {
        return buttonSquares;
    }

    @Override
    public void setClientGUI(ClientGUI clientGUI) {
    }

    @Override
    public void changeScene(Scene scene) {

    }

    @Override
    public Scene getScene() {
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 5; j++) {
                ButtonSquare button = new ButtonSquare(playerID, new Coordinate(i, j));
                buttonSquares.add(button);
                gridWindow.add(button, i, j);
            }
        }
    }
}
