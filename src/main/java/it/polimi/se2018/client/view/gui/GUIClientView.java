package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.network.ClientConnection;
import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.network.messages.responses.*;

public class GUIClientView implements ResponseHandler, ClientView{
    private final int playerID;
    private ClientConnection clientConnection;

    public GUIClientView(int playerID){
        this.playerID = playerID;
    }

    @Override
    public void handleNetworkInput(Response response) {
        response.handle(this);
    }

    @Override
    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;

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
        //to be implemented
    }

    @Override
    public void handleResponse(InputResponse inputResponse) {
        //to be implemented
    }

    @Override
    public void handleResponse(ScoreBoardResponse scoreBoardResponse) {
        //to be implemented
    }
}
