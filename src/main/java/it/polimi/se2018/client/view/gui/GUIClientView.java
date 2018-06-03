package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.network.ClientConnection;
import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.mvc.controller.ModelView;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;
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
    private int playersNumber;
    private PrivateObjective privateObjective;
    private List<PublicObjective> publicObjectives;
    private List<ToolCard> toolCards;

    public GUIClientView(int playerID){
        this.playerID = playerID;
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
        //to be implemented
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
        //change from PlayerNameScene to SelectWindowScene
        sceneController.changeScene(sceneController.getScene());
        //to be implemented the part in the gui
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
}
