package it.polimi.se2018.client.view.gui.controllers;

import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Square;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is the controller of scenes of the windows of enemy players
 */
class WindowEnemySceneController implements Initializable{
    /**
     * This is the window of the enemy player
     */
    private final Square[][] playerWindow;

    @FXML
    private GridPane gridPane;

    @FXML
    private BorderPane borderPane;

    WindowEnemySceneController(Square[][] playerWindow){
        this.playerWindow = playerWindow;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(int row=0; row < playerWindow.length; row++) {
            for (int col = 0; col < playerWindow[0].length; col++) {
                Image image;
                if (playerWindow[row][col].isEmpty()) image = new Image(playerWindow[row][col].getConstraintPath());
                else {
                    Die die = playerWindow[row][col].getDie();
                    image = new Image("/dice/"+ die.getColor().getAbbreviation()+ die.getValue()+ ".png");
                }
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(35);
                imageView.setFitWidth(35);
                gridPane.add(imageView, col, row);
            }
        }

        borderPane.setStyle("-fx-background-image: url('otherimages/window_no_level.png')");
    }
}
