package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.view.gui.button.ButtonCheckUsability;
import it.polimi.se2018.client.view.gui.button.ButtonGame;
import it.polimi.se2018.client.view.gui.stategui.State;
import it.polimi.se2018.client.view.gui.stategui.StateTurn;
import it.polimi.se2018.mvc.controller.ModelView;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.requests.PassMessage;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.utils.exceptions.ChangeActionException;
import it.polimi.se2018.utils.exceptions.HaltException;
import javafx.scene.Scene;

import java.util.List;

public class GameSceneController implements SceneController{
    private GUIClientView guiClientView;
    private ModelView modelView;
    private List<ToolCard> toolCards;
    private int playerID;
    private int stateID;
    private Message message;
    private WindowSceneController windowSceneController;
    private List<ButtonGame> buttons;
    private ToolCardMessage toolCardMessage;
    private ToolCardGUI toolCardGUI;
    private State currentState;
    private ButtonCheckUsability buttonCheckUsability;

    public GameSceneController(GUIClientView guiClientView, int playerID) {
        this.guiClientView = guiClientView;
        this.playerID = playerID;
        toolCardGUI = new ToolCardGUI(playerID, this);
        currentState = new StateTurn(this);
        buttonCheckUsability = new ButtonCheckUsability(this);
    }

    public ModelView getModelView() {
        return modelView;
    }

    public ToolCardMessage getToolCardMessage() {
        return toolCardMessage;
    }

    public GUIClientView getGuiClientView() {
        return guiClientView;
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

    public Message getMessage() {
        return message;
    }

    public void disableAllButton(){
        for (ButtonGame button: buttons) {
            button.disarm();
        }
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public void setAllButton(){
        for (ButtonGame button : buttons) {
            button.checkCondition(buttonCheckUsability, currentState);
        }
    }

    public void sendMessage() {
        guiClientView.handleNetworkOutput(toolCardMessage);
        toolCardMessage = null;
    }

    //togliere le eccezioni e capire se servono o come gestirle
    public void useToolCard(int toolCardIndex) throws ChangeActionException, HaltException {
        ToolCard toolCard = toolCards.get(toolCardIndex);
        toolCardMessage = new ToolCardMessage(playerID, stateID, toolCardIndex);
        toolCard.handleGUI(toolCardGUI, toolCardIndex);
    }

    public void passTurnButtonClicked() {
        guiClientView.handleNetworkOutput(new PassMessage(playerID, guiClientView.getBoard().getStateID(), false));
        disableAllButton();
    }

    public void buttonSquareClicked() {
        //implement
    }

    public void setClientGUI(GUIClientView guiClientView) {
        this.guiClientView = guiClientView;
    }

    @Override
    public void setClientGUI(ClientGUI clientGUI) {
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
