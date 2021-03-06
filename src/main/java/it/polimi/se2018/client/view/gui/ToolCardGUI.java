package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.view.gui.controllers.GameSceneController;
import it.polimi.se2018.client.view.gui.stategui.State;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateRoundTracker;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateWindowEnd;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateWindowPlace;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateWindowStart;
import it.polimi.se2018.mvc.model.toolcards.*;
import javafx.application.Platform;
import java.util.ArrayList;
import java.util.List;

public class ToolCardGUI implements ToolCardGUIHandler {
    private final GameSceneController gameSceneController;

    public ToolCardGUI(GameSceneController gameSceneController) {
        this.gameSceneController = gameSceneController;
    }

    @Override
    public void getPlayerRequests(CopperFoilBurnisher toolCard, int toolCardNumber) {
        StateWindowStart stateWindowStart = new StateWindowStart(gameSceneController);
        StateWindowEnd stateWindowEnd = new StateWindowEnd(gameSceneController);
        List<State> states = new ArrayList<>();
        states.add(stateWindowEnd);
        stateWindowStart.setNextState(states);
        Platform.runLater(() -> {
            gameSceneController.getCurrentState().changeState(stateWindowStart);
            gameSceneController.setAllButton();
        });
    }

    @Override
    public void getPlayerRequests(CorkBackedStraightedge toolCard, int toolCardNumber) {
        StateWindowPlace stateWindowPlace = new StateWindowPlace(gameSceneController);
        Platform.runLater(() -> {
            gameSceneController.getCurrentState().changeState(stateWindowPlace);
            gameSceneController.setAllButton();
        });
    }

    @Override
    public void getPlayerRequests(EglomiseBrush toolCard, int toolCardNumber) {
        StateWindowStart stateWindowStart = new StateWindowStart(gameSceneController);
        StateWindowEnd stateWindowEnd = new StateWindowEnd(gameSceneController);
        List<State> states = new ArrayList<>();
        states.add(stateWindowEnd);
        stateWindowStart.setNextState(states);
        Platform.runLater(() -> {
            gameSceneController.getCurrentState().changeState(stateWindowStart);
            gameSceneController.setAllButton();
        });
    }

    @Override
    public void getPlayerRequests(FluxBrush toolCard, int toolCardNumber) {
        gameSceneController.sendToolCardMessage();
    }

    @Override
    public void getPlayerRequests(FluxRemover toolCard, int toolCardNumber) {
        gameSceneController.sendToolCardMessage();
    }

    @Override
    public void getPlayerRequests(GlazingHammer toolCard, int toolCardNumber) {
        gameSceneController.sendToolCardMessage();
    }

    @Override
    public void getPlayerRequests(GrindingStone toolCard, int toolCardNumber) {
        gameSceneController.sendToolCardMessage();
    }

    @Override
    public void getPlayerRequests(GrozingPliers toolCard, int toolCardNumber) {
        gameSceneController.createPlusOrMinusWindow();
    }

    @Override
    public void getPlayerRequests(Lathekin toolCard, int toolCardNumber) {
        StateWindowStart stateWindowStart = new StateWindowStart(gameSceneController);
        List<State> states = new ArrayList<>();
        states.add(new StateWindowEnd(gameSceneController));
        states.add(new StateWindowStart(gameSceneController));
        states.add(new StateWindowEnd(gameSceneController));
        stateWindowStart.setNextState(states);
        Platform.runLater(() -> {
            gameSceneController.getCurrentState().changeState(stateWindowStart);
            gameSceneController.setAllButton();
        });
    }

    @Override
    public void getPlayerRequests(LensCutter toolCard, int toolCardNumber) {
        StateRoundTracker stateRoundTracker = new StateRoundTracker(gameSceneController);
        stateRoundTracker.setNextState(new ArrayList<>());
        Platform.runLater(() -> {
            gameSceneController.getCurrentState().changeState(stateRoundTracker);
            gameSceneController.setAllButton();
        });
    }

    @Override
    public void getPlayerRequests(RunningPliers toolCard, int toolCardNumber) {
        gameSceneController.sendToolCardMessage();
    }

    @Override
    public void getPlayerRequests(TapWheel toolCard, int toolCardNumber) {
        gameSceneController.createOneOrTwo();
    }
}
