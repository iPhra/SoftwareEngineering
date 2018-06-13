package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.GUIClient;
import it.polimi.se2018.client.view.gui.button.ButtonGame;
import it.polimi.se2018.client.view.gui.stategui.State;
import it.polimi.se2018.client.view.gui.stategui.StateTurn;
import it.polimi.se2018.mvc.controller.ModelView;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.PassMessage;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.utils.exceptions.ChangeActionException;
import it.polimi.se2018.utils.exceptions.HaltException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameSceneController implements SceneController, Initializable{
    private final GUIView guiView;
    private final GUIModel guiModel;
    private ModelView modelView;
    private List<ToolCard> toolCards;
    private final int playerID;
    private int stateID;
    private WindowSceneController windowSceneController;
    private List<ButtonGame> buttons;
    private ToolCardMessage toolCardMessage;
    private final ToolCardGUI toolCardGUI;
    private State currentState;
    private Stage stage;

    private BorderPane borderPane;


    public GameSceneController(GUIController guiController, GUIModel guiModel, int playerID) {
        this.guiView = guiController.getGuiView();
        this.guiModel = guiModel;
        this.playerID = playerID;
        toolCardGUI = new ToolCardGUI(this);
        currentState = new StateTurn(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GridPane topGridPane = new GridPane();
        List<Square[][]> enemyWindows = new ArrayList<>(guiModel.getBoard().getPlayerWindows());
        enemyWindows.remove(guiModel.getBoard().getPlayerID().indexOf(playerID));
        for(int i=0; i < enemyWindows.size(); i++){
            FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/windowEnemysScene.fxml")));
            loader.setController(new WindowEnemysSceneController(guiModel.getBoard().getPlayerWindows().get(i)));
            try {
                Node node = loader.load();
                Pane pane = new Pane();
                pane.getChildren().add(node);
                pane.setMaxWidth(206);
                pane.setMaxHeight(182);
                topGridPane.add(pane,i,0);
            } catch (IOException e) {
                Logger logger = Logger.getAnonymousLogger();
                logger.log(Level.ALL,e.getMessage());
            }
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public ModelView getModelView() {
        return modelView;
    }

    public ToolCardMessage getToolCardMessage() {
        return toolCardMessage;
    }

    public GUIView getGuiView() {
        return guiView;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getStateID() {
        return stateID;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void disableAllButton(){
        for (ButtonGame button: buttons) {
            button.disarm();
        }
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public void setToolCardMessage(ToolCardMessage toolCardMessage) {
        this.toolCardMessage = toolCardMessage;
    }

    public void setAllButton(){
        for (ButtonGame button : buttons) {
            button.checkCondition(currentState.getButtonCheckUsabilityHandler());
        }
    }

    public void sendToolCardMessage() {
        guiView.handleNetworkOutput(toolCardMessage);
        toolCardMessage = null;
    }

    //togliere le eccezioni e capire se servono o come gestirle
    //This method is called by network input when you receive an ack that allow you to use the toolcard
    public void useToolCard(int toolCardIndex) throws ChangeActionException, HaltException {
        ToolCard toolCard = toolCards.get(toolCardIndex);
        toolCardMessage = new ToolCardMessage(playerID, stateID, toolCardIndex);
        toolCard.handleGUI(toolCardGUI, toolCardIndex);
    }

    //This method is called by the controller of the button pass turn
    public void passTurnButtonClicked() {
        guiView.handleNetworkOutput(new PassMessage(playerID, guiModel.getBoard().getStateID(), false));
        disableAllButton();
    }


    //This method is called by controller of square button and round tracker button
    //Current state know how handle input
    public void buttonCoordinateClicked(Coordinate coordinate) {
        currentState.doActionWindow(coordinate);
    }

    //This method is called by controller of draft pool button
    public void buttonDraftPoolClicked(int drafPoolPosition){
        currentState.doActionDraftPool(drafPoolPosition);
    }

    //This method is called by controller of tool cards
    public void buttonToolCardClicked(int toolCardIndex){
        currentState.doActionToolCard(toolCardIndex);
    }

    public void setClientGUI(GUIClient guiClient) {
        //implement
    }

    @Override
    public void changeScene(Scene scene) {
        //implement
    }

    @Override
    public Scene getScene() {
        return null;
    }
}
