package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.GUIClient;
import it.polimi.se2018.client.view.gui.button.ButtonGame;
import it.polimi.se2018.client.view.gui.stategui.State;
import it.polimi.se2018.client.view.gui.stategui.StateTurn;
import it.polimi.se2018.mvc.controller.ModelView;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.PassMessage;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.utils.exceptions.ChangeActionException;
import it.polimi.se2018.utils.exceptions.HaltException;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class GameSceneController implements SceneController{
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

    public GameSceneController(GUIController guiController, GUIModel guiModel, int playerID) {
        this.guiView = guiController.getGuiView();
        this.guiModel = guiModel;
        this.playerID = playerID;
        toolCardGUI = new ToolCardGUI(this);
        currentState = new StateTurn(this);
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
