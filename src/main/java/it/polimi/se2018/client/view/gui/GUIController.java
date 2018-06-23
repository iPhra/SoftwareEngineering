package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.view.gui.stategui.StateTurn;
import it.polimi.se2018.mvc.controller.ModelView;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.network.messages.responses.sync.*;
import it.polimi.se2018.network.messages.responses.sync.modelupdates.*;
import it.polimi.se2018.utils.Observer;
import javafx.application.Platform;

import java.util.List;

public class GUIController implements SyncResponseHandler, Observer<SyncResponse> {
    private final int playerID;
    private final GUIView guiView;
    private final GUIModel guiModel;
    private SceneController sceneController;
    private List<Window> windows;
    private boolean gameStarted;
    //todo edo metti qualcosa di simile a toolCardPlayerInput

    GUIController(GUIView guiView, GUIModel guiModel, int playerID){
        this.playerID = playerID;
        this.guiView = guiView;
        this.guiModel = guiModel;
        gameStarted = false;
    }

    public GUIModel getGuiModel() {
        return guiModel;
    }

    public GUIView getGuiView() {
        return guiView;
    }

    public int getPlayerID() {
        return playerID;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setSceneController(SceneController sceneController){
        this.sceneController = sceneController;
    }

    public void setWindows(List<Window> windows) {
        this.windows = windows;
    }

    private void checkTurn() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (playerID != guiModel.getBoard().getCurrentPlayerID()) ((GameSceneController) sceneController).disableAllButton();
                else ((GameSceneController) sceneController).setAllButton();
            }
        });
    }

    public void refreshText(String description) {
        ((GameSceneController) sceneController).setText(description);
    }

    @Override
    public void handleResponse(ModelViewResponse modelViewResponse) {
        guiModel.setBoard(modelViewResponse.getModelView());
        guiModel.setPrivateObjective(modelViewResponse.getPrivateObjective());
        guiModel.setPublicObjectives(modelViewResponse.getPublicObjectives());
        guiModel.setToolCards(modelViewResponse.getToolCards());
        if (!gameStarted){
            gameStarted = true;
            sceneController.changeScene(sceneController.getScene()); //change the scene from SelectWindowScene to GameScene
        }
        else {
            ((GameSceneController) sceneController).refreshAll();
        }
        checkTurn();
        String message = "";
        if (modelViewResponse.getDescription().contains("passed")) message = "Round ends, ";
        else message = "Started, ";
        refreshText(message + (guiModel.getBoard().getCurrentPlayerID() == playerID ? "it's your turn" : "it's not your turn"));
        ((GameSceneController) sceneController).setCurrentState(new StateTurn((GameSceneController) sceneController));
    }

    @Override
    public void handleResponse(TextResponse textResponse) {
        refreshText("Invalid move");
        ((GameSceneController)sceneController).setCurrentState(new StateTurn((GameSceneController) sceneController));
        checkTurn();
    }

    @Override
    public void handleResponse(ToolCardResponse toolCardResponse) {
        ((GameSceneController) sceneController).useToolCard(toolCardResponse.getToolCardNumber());
        //todo edo cambia stato e permetti di usare la tool card adesso
    }

    @Override
    public void handleResponse(SetupResponse setupResponse) {
        guiModel.setPrivateObjective(setupResponse.getPrivateObjective());
        guiModel.setPlayersNumber(setupResponse.getPlayersNumber());
        setWindows(setupResponse.getWindows());
        ((PlayerNameSceneController) sceneController).setWindows(windows);
        ((PlayerNameSceneController) sceneController).setPrivateObjective(setupResponse.getPrivateObjective());
        sceneController.changeScene(sceneController.getScene()); //change from PlayerNameScene to SelectWindowScene1
    }

    @Override
    public void handleResponse(InputResponse inputResponse) {
        refreshText("Color of the die is " + inputResponse.getColor()+"\n");
        /* todo edo cosa devo fare qui?
        int choice = cliView.getDieValue();
        cliView.handleNetworkOutput(new InputMessage(playerID, cliModel.getBoard().getStateID(), choice));
        */
    }

    @Override
    public void handleResponse(ScoreBoardResponse scoreBoardResponse) {
        if(!scoreBoardResponse.isLastPlayer()) {
            ((GameSceneController) sceneController).setSortedPlayersNames(scoreBoardResponse.getSortedPlayersNames());
            ((GameSceneController) sceneController).setSortedPlayersScores(scoreBoardResponse.getSortedPlayersScores());
            sceneController.changeScene(sceneController.getScene());
        }
        guiView.endGame();
    }

    @Override
    public void handleResponse(ReconnectionResponse reconnectionResponse) {
        guiModel.setPlayersNumber(reconnectionResponse.getPlayersNumber());
        if(reconnectionResponse.isWindowsChosen()) {
            ModelViewResponse response = reconnectionResponse.getModelViewResponse();
            response.setDescription("Reconnected\n");
            handleResponse(response);
        }
        else refreshText("\nReconnected\n\n");
        ((GameSceneController)sceneController).setCurrentState(new StateTurn((GameSceneController) sceneController));
        checkTurn();
    }

    @Override
    public void handleResponse(DraftPoolResponse draftPoolResponse) {
        ModelView modelView = guiModel.getBoard();
        modelView.setHasDieInHand(draftPoolResponse.hasDieInHand());
        modelView.setHasDraftedDie(draftPoolResponse.hasDraftedDie());
        modelView.setHasUsedCard(draftPoolResponse.hasUsedCard());
        modelView.setDieInHand(draftPoolResponse.getDieInHand());
        modelView.setToolCardUsability(draftPoolResponse.getToolCardUsability());
        modelView.setPlayerFavorPoint(modelView.getPlayerID().indexOf(draftPoolResponse.getCurrentPlayerID()),draftPoolResponse.getFavorPoints());
        modelView.setStateID(draftPoolResponse.getStateID());
        modelView.setCurrentPlayerID(draftPoolResponse.getCurrentPlayerID());
        modelView.setDraftPool(draftPoolResponse.getDraftPool());
        ((GameSceneController) sceneController).refreshDraftPool();
        ((GameSceneController) sceneController).refreshFavorPointsAndDieInHand();
        String message = draftPoolResponse.getDescription();
        if(message.contains("passed")) refreshText("Turn ends, "+(modelView.getCurrentPlayerID()==playerID? "it's your turn" : "it's not your turn"));
        else refreshText(message);
        ((GameSceneController)sceneController).setCurrentState(new StateTurn((GameSceneController) sceneController));
        checkTurn();
    }

    @Override
    public void handleResponse(RoundTrackerResponse roundTrackerResponse) {
        ModelView modelView = guiModel.getBoard();
        modelView.setHasDieInHand(roundTrackerResponse.hasDieInHand());
        modelView.setHasDraftedDie(roundTrackerResponse.hasDraftedDie());
        modelView.setHasUsedCard(roundTrackerResponse.hasUsedCard());
        modelView.setDieInHand(roundTrackerResponse.getDieInHand());
        modelView.setToolCardUsability(roundTrackerResponse.getToolCardUsability());
        modelView.setPlayerFavorPoint(modelView.getPlayerID().indexOf(roundTrackerResponse.getCurrentPlayerID()),roundTrackerResponse.getFavorPoints());
        modelView.setStateID(roundTrackerResponse.getStateID());
        modelView.setCurrentPlayerID(roundTrackerResponse.getCurrentPlayerID());
        modelView.setRoundTracker(roundTrackerResponse.getRoundTracker());
        ((GameSceneController) sceneController).refreshRoundTracker();
        ((GameSceneController) sceneController).refreshFavorPointsAndDieInHand();
        refreshText("Round Tracker has been updated");
        ((GameSceneController)sceneController).setCurrentState(new StateTurn((GameSceneController) sceneController));
        checkTurn();
    }

    @Override
    public void handleResponse(WindowResponse windowResponse) {
        ModelView modelView = guiModel.getBoard();
        modelView.setHasDieInHand(windowResponse.hasDieInHand());
        modelView.setHasDraftedDie(windowResponse.hasDraftedDie());
        modelView.setHasUsedCard(windowResponse.hasUsedCard());
        modelView.setDieInHand(windowResponse.getDieInHand());
        modelView.setToolCardUsability(windowResponse.getToolCardUsability());
        modelView.setPlayerFavorPoint(modelView.getPlayerID().indexOf(windowResponse.getCurrentPlayerID()),windowResponse.getFavorPoints());
        modelView.setStateID(windowResponse.getStateID());
        modelView.setCurrentPlayerID(windowResponse.getCurrentPlayerID());
        modelView.setPlayerWindow(modelView.getPlayerID().indexOf(windowResponse.getCurrentPlayerID()),windowResponse.getWindow());
        //todo quando emilio implementa il metodo giusto, cambiare refreshAll con il refresh della window e quello del dado in mano
        ((GameSceneController) sceneController).refreshAll();
        refreshText("Windows have been updated");
        ((GameSceneController)sceneController).setCurrentState(new StateTurn((GameSceneController) sceneController));
        checkTurn();
    }

    @Override
    public void handleResponse(ModelUpdateResponse modelUpdateResponse) {
        ModelView modelView = guiModel.getBoard();
        modelView.setHasDieInHand(modelUpdateResponse.hasDieInHand());
        modelView.setHasDraftedDie(modelUpdateResponse.hasDraftedDie());
        modelView.setHasUsedCard(modelUpdateResponse.hasUsedCard());
        modelView.setDieInHand(modelUpdateResponse.getDieInHand());
        modelView.setToolCardUsability(modelUpdateResponse.getToolCardUsability());
        modelView.setPlayerFavorPoint(modelView.getPlayerID().indexOf(modelUpdateResponse.getCurrentPlayerID()),modelUpdateResponse.getFavorPoints());
        modelView.setStateID(modelUpdateResponse.getStateID());
        modelView.setCurrentPlayerID(modelUpdateResponse.getCurrentPlayerID());
        ((GameSceneController) sceneController).refreshFavorPointsAndDieInHand();
        ((GameSceneController)sceneController).setCurrentState(new StateTurn((GameSceneController) sceneController));
        checkTurn();
    }

    @Override
    public void update(SyncResponse message) {
        message.handle(this);
    }
}
