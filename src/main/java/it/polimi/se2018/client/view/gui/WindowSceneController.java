package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.view.gui.button.ButtonSquare;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class WindowSceneController implements Initializable {
    private final List<ButtonSquare> buttons;
    private final GameSceneController gameSceneController;

    @FXML
    private GridPane gridPane;

    @FXML
    private BorderPane borderPane;

    WindowSceneController(List<ButtonSquare> buttons, GameSceneController gameSceneController){
        this.buttons = buttons;
        this.gameSceneController = gameSceneController;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(ButtonSquare buttonSquare : buttons){
            gridPane.add(buttonSquare,buttonSquare.getCoordinate().getCol(),buttonSquare.getCoordinate().getRow());
            buttonSquare.setOnAction(e -> gameSceneController.buttonCoordinateClicked(((ButtonSquare)e.getSource()).getCoordinate()));
        }
        borderPane.setStyle("-fx-background-image: url('otherimages/window_no_level.png')");
    }
}