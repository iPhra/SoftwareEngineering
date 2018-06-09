package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.mvc.model.Die;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RoundTrackerSceneController implements Initializable {
    private List<List<Die>> roundTracker;
    private List<MenuButton> menuButtonList;

    @FXML
    private GridPane gridPane;

    RoundTrackerSceneController(List<List<Die>> roundTracker){
        this.roundTracker = roundTracker;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int i=0;
        menuButtonList = new ArrayList<>();
        for(List<Die> dieList : roundTracker){
            MenuButton menuButton = new MenuButton();
            for (Die die : dieList){
                Label label = new Label();
                label.setStyle("-fx-background-image:url('./dice/"+ die.getColor().getAbbreviation()+ die.getValue()+ ".png');");
                label.setPrefHeight(50);
                label.setPrefWidth(50);
                MenuItem menuItem = new MenuItem();
                menuItem.setGraphic(label);
                menuButton.getItems().add(menuItem);
                menuButton.setStyle("-fx-background-color : transparent;");
            }
            menuButtonList.add(menuButton);
            gridPane.add(menuButton,9-i,0);
            GridPane.setHalignment(menuButton, HPos.CENTER);
            GridPane.setValignment(menuButton, VPos.CENTER);
            i++;
        }
    }
}
