package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.network.ClientConnection;
import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.mvc.controller.ModelView;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;
import it.polimi.se2018.network.messages.requests.SetupMessage;
import it.polimi.se2018.network.messages.responses.sync.*;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.*;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.PrivateObjective;
import it.polimi.se2018.mvc.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.network.messages.responses.*;

import java.util.List;

public class GUIClientView implements SyncResponseHandler, ClientView {
    private final int playerID;
    private ClientConnection clientConnection;
    private SceneController sceneController;
    private ModelView board;
    private List<Window> windows;
    private boolean isGameStarted;
    private int playersNumber;
    private PrivateObjective privateObjective;
    private List<PublicObjective> publicObjectives;
    private List<ToolCard> toolCards;

    public GUIClientView(int playerID){
        this.playerID = playerID;
        isGameStarted = false;
    }

    public int getPlayerID() {
        return playerID;
    }

    public ModelView getBoard() {
        return board;
    }

    public void setBoard(ModelView modelView) {
        this.board = modelView;
    }

    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    public void setPrivateObjective(PrivateObjective privateObjective) {
        this.privateObjective = privateObjective;
    }

    public void setPublicObjectives(List<PublicObjective> publicObjectives) {
        this.publicObjectives = publicObjectives;
    }

    public void setToolCards(List<ToolCard> toolCards) {
        this.toolCards = toolCards;
    }

    public void setSceneController(SceneController sceneController){
        this.sceneController = sceneController;
    }

    public void setWindows(List<Window> windows) {
        this.windows = windows;
    }

    //called by SelectWindowSceneController when a window is chosen
    public void setWindowNumber(int windowNumber){
        handleNetworkOutput(new SetupMessage(playerID,0,windows.get(windowNumber)));
    }

    @Override
    public void handleNetworkInput(SyncResponse syncResponse) {
        syncResponse.handle(this);
    }

    @Override
    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;

    }

    public void handleNetworkOutput(Message message) {
        clientConnection.sendMessage(message);
    }

    @Override
    public void stop() {
        //implement
    }

    @Override
    public void handleResponse(ModelViewResponse modelViewResponse) {
        if (!isGameStarted){
            isGameStarted = true;
            //change the scene from SelectWindowScene to GameScene
            sceneController.changeScene(sceneController.getScene());
        }
        //the rest needs to be implemented
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
        setPrivateObjective(setupResponse.getPrivateObjective());
        setPublicObjectives(setupResponse.getPublicObjectives());
        setToolCards(setupResponse.getToolCards());
        setPlayersNumber(setupResponse.getPlayersNumber());
        setWindows(setupResponse.getWindows());
        //change from PlayerNameScene to SelectWindowScene
        ((PlayerNameSceneController) sceneController).setWindows(windows);
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
    public synchronized void handleAsyncEvent(boolean halt, String message) {
        //halt è true se è la fine del turno, quindi devo fermare il client
        //message è il messaggio da pritnare a schermo, tipo "Player a si è disconnesso"
        //questo metodo viene chiamato da AsyncStopper
    }
}
