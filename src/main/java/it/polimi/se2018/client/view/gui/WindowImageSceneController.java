package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.network.messages.Coordinate;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;

public class WindowImageSceneController implements Initializable{

    private Window window;
    @FXML
    private GridPane gridPane;

    WindowImageSceneController(Window window){
        this.window = window;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ImageView[][] gridImageView = new ImageView[4][5];
        for(int x=0;x < 4; x++){
            for (int y=0; y< 5; y++){
                Image image = new Image(window.getSquare(new Coordinate(x,y)).getConstraintPath());
                ImageView imageView = new ImageView(image);
                gridImageView[x][y] = imageView;
                imageView.setFitHeight(35);
                imageView.setFitWidth(35);

                gridPane.add(gridImageView[x][y],y,x);
                GridPane.setHalignment(gridImageView[x][y], HPos.CENTER);
                GridPane.setValignment(gridImageView[x][y], VPos.CENTER);
                gridPane.setMargin(gridImageView[x][y], new Insets(0,19,20,0));
            }
        }
        gridPane.setStyle("-fx-background-image: url('"+ window.getLevelPath() + "')");
    }
}
