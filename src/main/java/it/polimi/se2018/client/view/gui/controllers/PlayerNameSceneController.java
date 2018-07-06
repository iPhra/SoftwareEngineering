package it.polimi.se2018.client.view.gui.controllers;

import it.polimi.se2018.client.GUIClient;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.PrivateObjective;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the controller of the scene that asks to the player the nickname that he wants to use during the game
 */
public class PlayerNameSceneController implements SceneController {
    private static final String PLAYER_NAME_SCENE_CONTROLLER = "PlayerNameSceneController";
    private GUIClient guiClient;
    private Stage stage;
    private List<Window> windows;
    private PrivateObjective privateObjective;
    private boolean windowSelectionOver;
    private boolean reconnecting;
    private final Logger logger = Logger.getAnonymousLogger();

    @FXML
    private TextField nickTextField;

    @FXML
    private Label label;

    /**
     * called normally when not reconnecting
     * @param scene it's the scene that has to be changed
     */
    private void toWindowScene(Scene scene) {
        windowSelectionOver = false;
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/selectWindowScene.fxml")));
        try {
            SelectWindowSceneController selectWindowSceneController = new SelectWindowSceneController(windows, privateObjective, (guiClient.getGUIView()).getGuiLogic());
            selectWindowSceneController.setClientGUI(guiClient);
            loader.setController(selectWindowSceneController);
            Parent root = loader.load();
            guiClient.getGUIView().getGuiLogic().setSceneController(selectWindowSceneController);
            stage.setWidth(1000);
            stage.setHeight(700);
            selectWindowSceneController.setStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            logger.log(Level.SEVERE, PLAYER_NAME_SCENE_CONTROLLER,e);
        }
    }

    /**
     * called if player is reconnecting when the game is already after window selection
     * @param scene it's the scene that has to be changed
     */
    private void toMatchScene(Scene scene) {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/GameScene.fxml")));
        try {
            GameSceneController gameSceneController = new GameSceneController(guiClient.getGUIView().getGuiLogic());
            gameSceneController.setClientGUI(guiClient);
            loader.setController(gameSceneController);
            Parent root = loader.load();
            guiClient.getGUIView().getGuiLogic().setSceneController(gameSceneController);
            stage.setMinWidth(1440);
            stage.setMinHeight(900);
            stage.setMaximized(true);
            stage.setResizable(true);
            gameSceneController.setStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            logger.log(Level.SEVERE, PLAYER_NAME_SCENE_CONTROLLER,e);
        }
    }

    /**
     * called when reconnecting during window selecton
     * @param scene it's the scene that has to be changed
     */
    private void toReconnectionScene(Scene scene) {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/reconnectionScene.fxml")));
        try {
            Parent root = loader.load();
            ReconnectionSceneController reconnectionSceneController = loader.getController();
            reconnectionSceneController.setClientGUI(guiClient);
            loader.setController(reconnectionSceneController);
            guiClient.getGUIView().getGuiLogic().setSceneController(reconnectionSceneController);
            stage.setWidth(600);
            stage.setHeight(623);
            reconnectionSceneController.setStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            logger.log(Level.SEVERE, PLAYER_NAME_SCENE_CONTROLLER,e);
        }
    }

    public void setWindows(List<Window> windows) {
        this.windows = windows;
    }

    public void setPrivateObjective(PrivateObjective privateObjective) {
        this.privateObjective = privateObjective;
    }

    public void setGuiClient(GUIClient guiClient) {
        this.guiClient = guiClient;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setReconnecting() {
        reconnecting = true;
    }

    public void setWindowSelectionOver() {
        windowSelectionOver = true;
    }

    /**
     * This method is called when player wrote his nickname and pressed enter
     */
    public void onReturnHandler(){
        if (!guiClient.setPlayerName(nickTextField.getText())) {
            guiClient.getGUIView().getGuiLogic().setSceneController(this);
            label.setText("Your nickname is ok. Waiting for other players");
            nickTextField.setEditable(false);
        } else label.setText("Nickname already taken, choose another one");
    }

    @Override
    public Scene getScene(){
        return nickTextField.getScene();
    }

    @Override
    public void changeScene(Scene scene) {
        if(!reconnecting) toWindowScene(scene);
        else if (windowSelectionOver) toMatchScene(scene);
        else toReconnectionScene(scene);
    }
}


