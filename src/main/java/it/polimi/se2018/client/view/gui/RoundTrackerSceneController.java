package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.mvc.model.Die;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;

import java.net.URL;
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
        for(List<Die> dieList : roundTracker){
            MenuButton menuButton = new MenuButton();
            for (Die die : dieList){
                MenuItem menuItem = new MenuItem();
                menuItem.setStyle("-fx-background-image: "+ die.getColor().getAbbreviation()+ die.getValue()+ ".png;");
                menuButton.getItems().add(menuItem);
                menuButton.setStyle("-fx-background-color : transparent;");
            }
            menuButtonList.add(menuButton);
            gridPane.add(menuButton,i,0);
            i++;
        }
    }
}
