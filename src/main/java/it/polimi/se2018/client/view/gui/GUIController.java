package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.network.ClientConnection;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.network.messages.requests.SetupMessage;
import it.polimi.se2018.network.messages.responses.sync.*;
import it.polimi.se2018.network.messages.responses.sync.modelupdates.*;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.utils.Stopper;

import java.util.List;

public class GUIController implements SyncResponseHandler, Observer<SyncResponse>, Stopper {
    private final int playerID;
    private final GUIView guiView;
    private final GUIModel guiModel;
    private ClientConnection clientConnection;
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

    //called by SelectWindowSceneController when a window is chosen
    public void setWindowNumber(int windowNumber){
        guiView.handleNetworkOutput(new SetupMessage(playerID,0,windows.get(windowNumber)));
    }

    @Override
    public void handleResponse(ModelViewResponse modelViewResponse) {
        guiModel.setBoard(modelViewResponse.getModelView());
        guiModel.setPrivateObjective(modelViewResponse.getPrivateObjective());
        guiModel.setPublicObjectives(modelViewResponse.getPublicObjectives());
        guiModel.setToolCards(modelViewResponse.getToolCards());
        if (!isGameStarted){
            isGameStarted = true;
            //change the scene from SelectWindowScene to GameScene
            sceneController.changeScene(sceneController.getScene());
        }
        //the else needs to be implemented
    }

    @Override
    public void handleResponse(TextResponse textResponse) {
        //to be implemented
    }

    @Override
    public void handleResponse(ToolCardResponse toolCardResponse) {
        //to be implemented
    }

    @Override
    public void handleResponse(SetupResponse setupResponse) {
        guiModel.setPrivateObjective(setupResponse.getPrivateObjective());
        guiModel.setPlayersNumber(setupResponse.getPlayersNumber());
        setWindows(setupResponse.getWindows());
        //change from PlayerNameScene to SelectWindowScene
        ((PlayerNameSceneController) sceneController).setWindows(windows);
        ((PlayerNameSceneController) sceneController).setPrivateObjective(setupResponse.getPrivateObjective());
        sceneController.changeScene(sceneController.getScene());
    }

    @Override
    public void handleResponse(InputResponse inputResponse) {
        //to be implemented
    }

    @Override
    public void handleResponse(ScoreBoardResponse scoreBoardResponse) {
        //to be implemented
    }

    /**
     * This method is used by the Server to communicate that a specific player has disconnected
     *
     * @param reconnectionResponse contains a notification message
     */
    @Override
    public void handleResponse(ReconnectionResponse reconnectionResponse) {
        //implementa
    }

    @Override
    public void handleResponse(DraftPoolResponse draftPoolResponse) {
        //implement
    }

    @Override
    public void handleResponse(RoundTrackerResponse roundTrackerResponse) {
        //implement
    }

    @Override
    public void handleResponse(WindowResponse windowResponse) {
        //implement
    }

    @Override
    public void handleResponse(ModelUpdateResponse modelUpdateResponse) {
        //implement
    }

    @Override
    public void update(SyncResponse message) {
        message.handle(this);
    }

    @Override
    public void halt(String message) {
        //implement
    }
}
