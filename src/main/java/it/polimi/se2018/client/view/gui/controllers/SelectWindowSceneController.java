package it.polimi.se2018.client.view.gui.controllers;

import it.polimi.se2018.client.view.gui.GUILogic;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.PrivateObjective;
import it.polimi.se2018.network.messages.requests.SetupMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the controller of the scene that allows the player to choose his window amongst four
 */
public class SelectWindowSceneController extends MatchHandler implements SceneController, Initializable {
    private static final String SELECT_WINDOW_SCENE_CONTROLLER = "SelectWindowSceneController";
    private final GUILogic guiLogic;
    private final List<Window> windows;
    private final PrivateObjective privateObjective;
    private Button[] buttons;

    @SuppressWarnings("unused")
    @FXML
    private GridPane gridPane;

    @SuppressWarnings("unused")
    @FXML
    private Label label;

    @SuppressWarnings("unused")
    @FXML
    private BorderPane borderPane;

    SelectWindowSceneController(List<Window> windows, PrivateObjective privateObjective, GUILogic guiLogic){
        this.windows = windows;
        this.privateObjective = privateObjective;
        this.guiLogic = guiLogic;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        borderPane.setPadding(new Insets(0,0,30,0));
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
    }

    /**
     * This method is called when player chose his window. It sends that to the server
     * @param windowNumber
     */
    private void windowChosen(int windowNumber){
        for(Button button : buttons){
            button.setDisable(true);
        }
        label.setText("You chose window "+ windows.get(windowNumber).getTitle() + "! Waiting for other players.");
        guiLogic.getGuiView().handleNetworkOutput(new SetupMessage(guiLogic.getPlayerID(),0,windows.get(windowNumber)));
    }

    /**
     * This method gets the gui to the ScoreBoardScene. It's called if every other player disconnected during window
     * selection. In that case, this player is considered the winner
     * @param scene it's the current scene that has to be changed
     */
    private void toScoreBoardScene(Scene scene) {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/ScoreBoardScene.fxml")));
            try {
                ScoreBoardSceneController scoreBoardSceneController = new ScoreBoardSceneController(sortedPlayersNames, sortedPlayersScores, isLastPlayer);
                scoreBoardSceneController.setGuiClient(guiClient);
                loader.setController(scoreBoardSceneController);
                Parent root = loader.load();
                guiClient.getGUIView().getGuiLogic().setSceneController(scoreBoardSceneController);
                stage.setWidth(600);
                stage.setHeight(623);
                scoreBoardSceneController.setStage(stage);
                scene.setRoot(root);
            } catch (IOException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, SELECT_WINDOW_SCENE_CONTROLLER);
            }
        });
    }

    /**
     * This method gets the gui to the GameScene. It's called if every player chose the window or if timer ran up
     * @param scene it's the current scene that has to be changed
     */
    private void toMatchScene(Scene scene) {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/GameScene.fxml")));
        try {
            GameSceneController gameSceneController = new GameSceneController(guiLogic);
            gameSceneController.setClientGUI(guiClient);
            loader.setController(gameSceneController);
            Parent root = loader.load();
            guiLogic.setSceneController(gameSceneController);
            stage.setMinWidth(1440);
            stage.setMinHeight(900);
            stage.setMaximized(true);
            stage.setResizable(true);
            gameSceneController.setStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, SELECT_WINDOW_SCENE_CONTROLLER);
        }
    }

    @Override
    public void changeScene(Scene scene) {
        if(toScoreBoardScene) toScoreBoardScene(scene);
        else toMatchScene(scene);
    }

    @Override
    public Scene getScene() {
        return gridPane.getScene();
    }
}
