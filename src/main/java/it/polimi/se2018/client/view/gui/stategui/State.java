package it.polimi.se2018.client.view.gui.stategui;

import it.polimi.se2018.client.view.gui.controllers.GameSceneController;
import it.polimi.se2018.client.view.gui.button.buttoncheckusability.ButtonCheckUsabilityHandler;
import it.polimi.se2018.network.messages.Coordinate;

import java.util.List;

/**
 * This class is generic State Gui's controller. It determines how GUI controller handle clicks on the differents
 * buttons
 */

public abstract class State {
    /**
     * This is the reference to the main scene of the GUI
     */
    protected GameSceneController gameSceneController;

    /**
     * This represent the list of state that will be after this
     * It is used when you have to select determinate button to use a Toolcard
     */
    protected List<State> nextState;

    /**
     * With this handler button can understand if they have to be clickablke or not
     */
    protected ButtonCheckUsabilityHandler buttonCheckUsabilityHandler;
    /**
     * This is the message that describe the state that is shown to the player
     */
    protected String descriptionOfState;

    /**
     * This methos fill the List of next State
     * @param nextState list of the state that will be
     */
    public void setNextState(List<State> nextState) {
        this.nextState = nextState;
    }

    /**
     * change the state of gameSceneController to the next onje
     * @param state the new state
     */
    public void changeState(State state){
        gameSceneController.setCurrentState(state);
        gameSceneController.setText(state.descriptionOfState);
        gameSceneController.setAllButton();
    }

    public ButtonCheckUsabilityHandler getButtonCheckUsabilityHandler() {
        return buttonCheckUsabilityHandler;
    }

    /**
     * This method determine how handle a click on your window
     * @param coordinate of the window that you select
     */
    public abstract void doActionWindow(Coordinate coordinate);

    /**
     * This method determine how handle a click on draftpool
     * @param draftPoolPosition is the position of the draftpool that the player select
     */
    public abstract void doActionDraftPool(int draftPoolPosition);

    /**
     * his method determine how handle a click on a toolcard
     * @param toolCardIndex is the index of the selected toolcard
     */
    public abstract void doActionToolCard(int toolCardIndex);
}
