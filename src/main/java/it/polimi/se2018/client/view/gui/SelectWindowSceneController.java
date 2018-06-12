package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.PrivateObjective;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SelectWindowSceneController implements SceneController, Initializable{
    private final GUIController guiController;
    private final List<Window> windows;
    private final PrivateObjective privateObjective;
    private Stage stage;
    private Button[] buttons;

    @FXML
    private GridPane gridPane;

    @FXML
    private Label label;

    @FXML
    private BorderPane borderPane;

    SelectWindowSceneController(List<Window> windows, PrivateObjective privateObjective, GUIController guiController){
        this.windows = windows;
        this.privateObjective = privateObjective;
        this.guiController = guiController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttons = new Button[4];
        for (int i=0;i<4; i++){
            FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/windowImageScene.fxml")));
            loader.setController(new WindowImageSceneController(windows.get(i)));
            try {
                Node node = loader.load();
                Pane pane = new Pane();
                pane.getChildren().add(node);
                pane.setMaxWidth(206);
                pane.setMaxHeight(182);
                gridPane.add(pane,i,0);
                buttons[i] = new Button(windows.get(i).getTitle());
                gridPane.add(buttons[i],i,1);
            } catch (IOException e) {
                Logger logger = Logger.getAnonymousLogger();
                logger.log(Level.ALL,e.getMessage());
            }
        }
        buttons[0].setOnAction( e -> windowChosen(0));
        buttons[1].setOnAction( e -> windowChosen(1));
        buttons[2].setOnAction( e -> windowChosen(2));
        buttons[3].setOnAction( e -> windowChosen(3));
        ImageView imageView = new ImageView(new Image(privateObjective.getImagePath()));
        imageView.setFitWidth(188);
        imageView.setFitHeight(263);
        borderPane.setBottom(imageView);
        BorderPane.setAlignment(imageView, Pos.TOP_CENTER);
        //imageView.maxHeight(200);
        //imageView.maxWidth(200);
    }

    private void windowChosen(int windowNumber){
        for(Button button : buttons){
            button.setDisable(true);
        }
        label.setText("you chose window "+ windows.get(windowNumber).getTitle() + "! Waiting for other players.");
        guiController.setWindowNumber(windowNumber);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void changeScene(Scene scene) {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/GameScene.fxml")));
        try {
            GameSceneController gameSceneController = new GameSceneController(guiController, guiController.getGuiModel(), guiController.getPlayerID());
            loader.setController(gameSceneController);
            Parent root = loader.load();
            guiController.setSceneController(gameSceneController);
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
