package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.view.gui.controllers.GameSceneController;
import it.polimi.se2018.client.view.gui.controllers.MatchHandler;
import it.polimi.se2018.client.view.gui.controllers.PlayerNameSceneController;
import it.polimi.se2018.client.view.gui.controllers.SceneController;
import it.polimi.se2018.client.view.gui.stategui.StateTurn;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.network.messages.responses.sync.*;
import it.polimi.se2018.network.messages.responses.sync.modelupdates.*;
import it.polimi.se2018.utils.Observer;
import javafx.application.Platform;

import java.util.List;

public class GUILogic implements SyncResponseHandler, Observer<SyncResponse> {
    private final int playerID;
    private final GUIView guiView;
    private final GUIData guiModel;
    private SceneController sceneController;
    private List<Window> windows;
    private boolean gameStarted;

    GUILogic(GUIView guiView, GUIData guiModel, int playerID) {
        this.playerID = playerID;
        this.guiView = guiView;
        this.guiModel = guiModel;
        gameStarted = false;
    }

    private void checkState() {
        if(!gameStarted){
            gameStarted = true;
            sceneController.changeScene(sceneController.getScene()); //from playerName to SelectWindowScene
        }
        else ((GameSceneController) sceneController).clearAndRefreshAll();
    }

    private void setWindows(List<Window> windows) {
        this.windows = windows;
    }

    public GUIData getGuiModel() {
        return guiModel;
    }

    public GUIView getGuiView() {
        return guiView;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setSceneController(SceneController sceneController){
        this.sceneController = sceneController;
    }

    public void refreshText(String description) {
        ((GameSceneController) sceneController).setText(description);
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public SceneController getSceneController() {
        return sceneController;
    }

    @Override
    public void handleResponse(ModelViewResponse modelViewResponse) {
        guiModel.setBoard(modelViewResponse.getModelView());
        guiModel.setPrivateObjective(modelViewResponse.getPrivateObjective());
        guiModel.setPublicObjectives(modelViewResponse.getPublicObjectives());
        guiModel.setToolCards(modelViewResponse.getToolCards());
        Platform.runLater(() -> {
            checkState();
            String message;
            if (modelViewResponse.getDescription().contains("passed")) message = "Round ends, ";
            else message = "Started, ";
            refreshText(message + (guiModel.getBoard().getCurrentPlayerID() == playerID ? "it's your turn" : "it's not your turn"));
            ((GameSceneController) sceneController).setCurrentState(new StateTurn((GameSceneController) sceneController));
        });

    }

    @Override
    public void handleResponse(TextResponse textResponse) {
        Platform.runLater(() -> {
            refreshText("Invalid move");
            ((GameSceneController)sceneController).setCurrentState(new StateTurn((GameSceneController) sceneController));
            ((GameSceneController)sceneController).setAllButton();
        });
    }

    @Override
    public void handleResponse(ToolCardResponse toolCardResponse) {
        ((GameSceneController) sceneController).useToolCard(toolCardResponse.getToolCardNumber());
    }

    @Override
    public void handleResponse(SetupResponse setupResponse) {
        guiModel.setPrivateObjective(setupResponse.getPrivateObjective());
        guiModel.setPlayersNumber(setupResponse.getPlayersNumber());
        setWindows(setupResponse.getWindows());
        ((PlayerNameSceneController) sceneController).setWindows(windows);
        ((PlayerNameSceneController) sceneController).setPrivateObjective(setupResponse.getPrivateObjective());
        Platform.runLater(() ->
            sceneController.changeScene(sceneController.getScene())); //change from PlayerNameScene to SelectWindowScene
    }

    @Override
    public void handleResponse(InputResponse inputResponse) {
        Platform.runLater(() -> refreshText("Color of the die is " + inputResponse.getColor()+"\n"));
        ((GameSceneController) sceneController).createNumberWindow();
    }

    @Override
    public void handleResponse(ScoreBoardResponse scoreBoardResponse) {
        if(!scoreBoardResponse.isLastPlayer()) {
            ((MatchHandler) sceneController).setSortedPlayersNames(scoreBoardResponse.getSortedPlayersNames());
            ((MatchHandler) sceneController).setSortedPlayersScores(scoreBoardResponse.getSortedPlayersScores());
        }
        ((MatchHandler) sceneController).setIsLastPlayer(scoreBoardResponse.isLastPlayer());
        ((MatchHandler) sceneController).setGameEnd();
        sceneController.changeScene(sceneController.getScene()); //from SelectWindowScene or GameScene to ScoreBoard
        guiView.endGame();
    }

    @Override
    public void handleResponse(ReconnectionResponse reconnectionResponse) {
        guiModel.setPlayersNumber(reconnectionResponse.getPlayersNumber());
        ((PlayerNameSceneController) sceneController).setReconnecting();
        if(reconnectionResponse.isWindowSelectionOver()) {
            ((PlayerNameSceneController) sceneController).setWindowSelectionOver();
            ModelViewResponse response = reconnectionResponse.getModelViewResponse();
            response.setDescription("Reconnected\n");
            handleResponse(response);
        }
        else sceneController.changeScene(sceneController.getScene());
    }

    @Override
    public void handleResponse(DraftPoolResponse draftPoolResponse) {
        ModelView modelView = guiModel.getBoard();
        modelView.setHasDieInHand(draftPoolResponse.hasDieInHand());
        modelView.setHasDraftedDie(draftPoolResponse.hasDraftedDie());
        modelView.setHasUsedCard(draftPoolResponse.hasUsedCard());
        modelView.setDieInHand(draftPoolResponse.getDieInHand());
        modelView.setToolCardUsability(draftPoolResponse.getToolCardUsability());
        modelView.setPlayerFavorPoint(modelView.getPlayerIDs().indexOf(draftPoolResponse.getCurrentPlayerID()),draftPoolResponse.getFavorPoints());
        modelView.setStateID(draftPoolResponse.getStateID());
        modelView.setCurrentPlayerID(draftPoolResponse.getCurrentPlayerID());
        modelView.setDraftPool(draftPoolResponse.getDraftPool());
        modelView.setToolCardUsage(draftPoolResponse.getToolCardUsage());
        Platform.runLater(() -> {
            ((GameSceneController) sceneController).clearAndRefreshAll();
            String message = draftPoolResponse.getDescription();
            if(message.contains("passed")) refreshText("Turn ends, "+(modelView.getCurrentPlayerID()==playerID? "it's your turn" : "it's not your turn"));
            else refreshText(message);
            ((GameSceneController)sceneController).setCurrentState(new StateTurn((GameSceneController) sceneController));
        });
    }

    @Override
    public void handleResponse(RoundTrackerResponse roundTrackerResponse) {
        ModelView modelView = guiModel.getBoard();
        modelView.setHasDieInHand(roundTrackerResponse.hasDieInHand());
        modelView.setHasDraftedDie(roundTrackerResponse.hasDraftedDie());
        modelView.setHasUsedCard(roundTrackerResponse.hasUsedCard());
        modelView.setDieInHand(roundTrackerResponse.getDieInHand());
        modelView.setToolCardUsability(roundTrackerResponse.getToolCardUsability());
        modelView.setPlayerFavorPoint(modelView.getPlayerIDs().indexOf(roundTrackerResponse.getCurrentPlayerID()),roundTrackerResponse.getFavorPoints());
        modelView.setStateID(roundTrackerResponse.getStateID());
        modelView.setCurrentPlayerID(roundTrackerResponse.getCurrentPlayerID());
        modelView.setRoundTracker(roundTrackerResponse.getRoundTracker());
        modelView.setToolCardUsage(roundTrackerResponse.getToolCardUsage());
        Platform.runLater(() -> {
            ((GameSceneController) sceneController).clearAndRefreshAll();
            refreshText("Round Tracker has been updated");
            ((GameSceneController)sceneController).setCurrentState(new StateTurn((GameSceneController) sceneController));
        });
    }

    @Override
    public void handleResponse(WindowResponse windowResponse) {
        ModelView modelView = guiModel.getBoard();
        modelView.setHasDieInHand(windowResponse.hasDieInHand());
        modelView.setHasDraftedDie(windowResponse.hasDraftedDie());
        modelView.setHasUsedCard(windowResponse.hasUsedCard());
        modelView.setDieInHand(windowResponse.getDieInHand());
        modelView.setToolCardUsability(windowResponse.getToolCardUsability());
        modelView.setPlayerFavorPoint(modelView.getPlayerIDs().indexOf(windowResponse.getCurrentPlayerID()),windowResponse.getFavorPoints());
        modelView.setStateID(windowResponse.getStateID());
        modelView.setCurrentPlayerID(windowResponse.getCurrentPlayerID());
        modelView.setPlayerWindow(modelView.getPlayerIDs().indexOf(windowResponse.getCurrentPlayerID()),windowResponse.getWindow());
        modelView.setToolCardUsage(windowResponse.getToolCardUsage());
        Platform.runLater(() -> {
            ((GameSceneController) sceneController).clearAndRefreshAll();
            refreshText("Windows have been updated");
            ((GameSceneController)sceneController).setCurrentState(new StateTurn((GameSceneController) sceneController));
        });
    }

    @Override
    public void handleResponse(ModelUpdateResponse modelUpdateResponse) {
        ModelView modelView = guiModel.getBoard();
        modelView.setHasDieInHand(modelUpdateResponse.hasDieInHand());
        modelView.setHasDraftedDie(modelUpdateResponse.hasDraftedDie());
        modelView.setHasUsedCard(modelUpdateResponse.hasUsedCard());
        modelView.setDieInHand(modelUpdateResponse.getDieInHand());
        modelView.setToolCardUsability(modelUpdateResponse.getToolCardUsability());
        modelView.setPlayerFavorPoint(modelView.getPlayerIDs().indexOf(modelUpdateResponse.getCurrentPlayerID()),modelUpdateResponse.getFavorPoints());
        modelView.setStateID(modelUpdateResponse.getStateID());
        modelView.setCurrentPlayerID(modelUpdateResponse.getCurrentPlayerID());
        modelView.setToolCardUsage(modelUpdateResponse.getToolCardUsage());
        Platform.runLater(() -> {
            ((GameSceneController) sceneController).clearAndRefreshAll();
            ((GameSceneController)sceneController).setCurrentState(new StateTurn((GameSceneController) sceneController));
        });
    }

    @Override
    public void update(SyncResponse message) {
        message.handle(this);
    }
}
