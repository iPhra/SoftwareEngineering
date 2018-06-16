package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.mvc.controller.ModelView;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.network.messages.responses.sync.*;
import it.polimi.se2018.network.messages.responses.sync.modelupdates.*;
import it.polimi.se2018.utils.Observer;

import java.util.List;

public class GUIController implements SyncResponseHandler, Observer<SyncResponse> {
    private static final String NOT_TURN = "\nIt's not your turn. You can't do anything";
    private final int playerID;
    private final GUIView guiView;
    private final GUIModel guiModel;
    private SceneController sceneController;
    private List<Window> windows;
    private boolean isGameStarted;
    //qualcosa equivalente a toolcardplayerinput

    GUIController(GUIView guiView, GUIModel guiModel, int playerID){
        this.playerID = playerID;
        this.guiView = guiView;
        this.guiModel = guiModel;
        isGameStarted = false;
    }

    public void refreshText(String description) {
        if (playerID == guiModel.getBoard().getCurrentPlayerID())
            ((GameSceneController) sceneController).setText(description);
        else ((GameSceneController) sceneController).setText(description + NOT_TURN);
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

    public void setSceneController(SceneController sceneController){
        this.sceneController = sceneController;
    }

    public void setWindows(List<Window> windows) {
        this.windows = windows;
    }

    @Override
    public void handleResponse(ModelViewResponse modelViewResponse) {
        guiModel.setBoard(modelViewResponse.getModelView());
        guiModel.setPrivateObjective(modelViewResponse.getPrivateObjective());
        guiModel.setPublicObjectives(modelViewResponse.getPublicObjectives());
        guiModel.setToolCards(modelViewResponse.getToolCards());
        if (!isGameStarted){
            isGameStarted = true;
            sceneController.changeScene(sceneController.getScene()); //change the scene from SelectWindowScene to GameScene
        }
        ((GameSceneController)sceneController).refresh();
        refreshText(modelViewResponse.getDescription());
    }

    @Override
    public void handleResponse(TextResponse textResponse) {
        refreshText(textResponse.getDescription());
    }

    @Override
    public void handleResponse(ToolCardResponse toolCardResponse) {
        //cambia stato e permetti di usare la tool card adesso
    }

    @Override
    public void handleResponse(SetupResponse setupResponse) {
        guiModel.setPrivateObjective(setupResponse.getPrivateObjective());
        guiModel.setPlayersNumber(setupResponse.getPlayersNumber());
        setWindows(setupResponse.getWindows());
        ((PlayerNameSceneController) sceneController).setWindows(windows);
        ((PlayerNameSceneController) sceneController).setPrivateObjective(setupResponse.getPrivateObjective());
        sceneController.changeScene(sceneController.getScene()); //change from PlayerNameScene to SelectWindowScene
    }

    @Override
    public void handleResponse(InputResponse inputResponse) {
        refreshText("Color of the die is " + inputResponse.getColor()+"\n");
        /* cosa devo fare qui?
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
            response.setDescription("Reconnected, wait for your turn\n");
            handleResponse(response);
        }
        else refreshText("\nReconnected, wait for other players to choose their Windows\n\n");
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
        //refresha la draft pool, i favor points e il dado in mano
        refreshText(draftPoolResponse.getDescription());
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
        //refresha il round tracker, i favor points e il dado in mano
        refreshText(roundTrackerResponse.getDescription());
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
        //refresha la window, i favor points e il dado in mano
        refreshText(windowResponse.getDescription());
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
        //refresha il dado in mano e i favor points
        refreshText(modelUpdateResponse.getDescription());
    }

    @Override
    public void update(SyncResponse message) {
        message.handle(this);
    }
}
