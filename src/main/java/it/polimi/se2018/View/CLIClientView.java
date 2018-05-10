package it.polimi.se2018.View;

import it.polimi.se2018.Network.Connections.ClientConnection;
import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Network.Messages.Responses.*;

import java.io.InputStream;
import java.io.OutputStream;

public class CLIClientView implements ResponseHandler {
    private final Player player;
    private final ClientConnection clientConnection;
    private final InputStream input;
    private final OutputStream output;

    public CLIClientView(Player player, ClientConnection clientConnection, InputStream input, OutputStream output) {
        this.player = player;
        this.clientConnection = clientConnection;
        this.input = input;
        this.output = output;
    }

    //receives input from the network, called by class clientConnection
    public void handleNetworkInput(Response response) {
        response.handle(this);
    }

    //called when i receive a TextResponse
    public void handleUserInput() {
    }

    //updates the board
    @Override
    public void handleResponse(ModelViewResponse modelViewResponse) {
    }

    //prints the text message
    @Override
    public void handleResponse(TextResponse textResponse) {
    }

    @Override
    public void handleResponse(TurnStartResponse turnStartResponse) {
    }

}
