package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.view.gui.stategui.State;
import it.polimi.se2018.client.view.gui.stategui.StateRoundTracker;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateWindowEnd;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateWindowPlace;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateWindowStart;
import it.polimi.se2018.mvc.model.toolcards.*;
import it.polimi.se2018.utils.exceptions.ChangeActionException;
import it.polimi.se2018.utils.exceptions.HaltException;

import java.util.ArrayList;
import java.util.List;

public class ToolCardGUI implements ToolCardGUIHandler {
    private final int playerID;
    private GameSceneController gameSceneController;

    public ToolCardGUI(int playerID, GameSceneController gameSceneController) {
        this.playerID = playerID;
        this.gameSceneController = gameSceneController;
    }


    @Override
    public void getPlayerRequests(CopperFoilBurnisher toolCard, int toolCardNumber) throws HaltException, ChangeActionException {
        StateWindowStart stateWindowStart = new StateWindowStart(gameSceneController);
        StateWindowEnd stateWindowEnd = new StateWindowEnd(gameSceneController);
        List<State> states = new ArrayList<>();
        states.add(stateWindowEnd);
        stateWindowStart.setNextState(states);
        gameSceneController.getCurrentState().changeState(stateWindowStart);
    }

    @Override
    public void getPlayerRequests(CorkBackedStraightedge toolCard, int toolCardNumber) throws HaltException, ChangeActionException {
        StateWindowPlace stateWindowPlace = new StateWindowPlace(gameSceneController);
        gameSceneController.getCurrentState().changeState(stateWindowPlace);
    }

    @Override
    public void getPlayerRequests(EglomiseBrush toolCard, int toolCardNumber) throws HaltException, ChangeActionException {
        StateWindowStart stateWindowStart = new StateWindowStart(gameSceneController);
        StateWindowEnd stateWindowEnd = new StateWindowEnd(gameSceneController);
        List<State> states = new ArrayList<>();
        states.add(stateWindowEnd);
        stateWindowStart.setNextState(states);
        gameSceneController.getCurrentState().changeState(stateWindowStart);
    }

    @Override
    public void getPlayerRequests(FluxBrush toolCard, int toolCardNumber) {
        gameSceneController.sendMessage();
    }

    @Override
    public void getPlayerRequests(FluxRemover toolCard, int toolCardNumber) {
        //TODO
    }

    @Override
    public void getPlayerRequests(GlazingHammer toolCard, int toolCardNumber) {
        gameSceneController.sendMessage();
    }

    @Override
    public void getPlayerRequests(GrindingStone toolCard, int toolCardNumber) {
        gameSceneController.sendMessage();
    }

    @Override
    public void getPlayerRequests(GrozingPliers toolCard, int toolCardNumber) throws HaltException {
        //TODO

    }

    @Override
    public void getPlayerRequests(Lathekin toolCard, int toolCardNumber) throws HaltException, ChangeActionException {
        StateWindowStart stateWindowStart = new StateWindowStart(gameSceneController);
        StateWindowEnd stateWindowEnd = new StateWindowEnd(gameSceneController);
        StateWindowStart stateWindowStart2 = new StateWindowStart(gameSceneController);
        StateWindowEnd stateWindowEnd2 = new StateWindowEnd(gameSceneController);
        List<State> states = new ArrayList<>();
        states.add(stateWindowEnd);
        states.add(stateWindowStart2);
        states.add(stateWindowEnd2);
        stateWindowStart.setNextState(states);
        gameSceneController.getCurrentState().changeState(stateWindowStart);
    }

    @Override
    public void getPlayerRequests(LensCutter toolCard, int toolCardNumber) throws HaltException, ChangeActionException {
        StateRoundTracker stateRoundTracker = new StateRoundTracker(gameSceneController);
        gameSceneController.getCurrentState().changeState(stateRoundTracker);
    }

    @Override
    public void getPlayerRequests(RunningPliers toolCard, int toolCardNumber) {
        gameSceneController.sendMessage();
    }

    @Override
    public void getPlayerRequests(TapWheel toolCard, int toolCardNumber) throws HaltException, ChangeActionException {
        StateWindowStart stateWindowStart = new StateWindowStart(gameSceneController);
        StateWindowEnd stateWindowEnd = new StateWindowEnd(gameSceneController);
        StateWindowStart stateWindowStart2 = new StateWindowStart(gameSceneController);
        StateWindowEnd stateWindowEnd2 = new StateWindowEnd(gameSceneController);
        StateRoundTracker stateRoundTracker = new StateRoundTracker(gameSceneController);
        List<State> states = new ArrayList<>();
        states.add(stateWindowEnd);
        states.add(stateWindowStart2);
        states.add(stateWindowEnd2);
        states.add(stateRoundTracker);
        stateWindowStart.setNextState(states);
        gameSceneController.getCurrentState().changeState(stateWindowStart);
    }
}
