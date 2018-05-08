package it.polimi.se2018.View;

import it.polimi.se2018.Connections.ServerConnection;
import it.polimi.se2018.Model.Messages.*;
import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Utils.Observable;
import it.polimi.se2018.Utils.Observer;


import java.util.Map;

public class ServerView extends Observable<Message> implements Observer<Response>, ResponseHandler{
    private Map<Player,ServerConnection> playerConnections;

    public ServerView(Map<Player,ServerConnection> playerConnections) {
        this.playerConnections = playerConnections;
    }

    public ServerConnection getPlayerConnection(Player player) {
        return playerConnections.get(player);
    }

    //receives input from the network, notifies the controller
    public void handleNetworkInput(Message input) {
        notify(input);
    }

    //receives a response, sends it to the clients by calling the right method
    @Override
    public void update(Response response) {
        response.handle(this);
    }

    //broadcasts the model to all the clients
    @Override
    public void handleResponse(ModelViewResponse response) {
    }

    //sends a text response to the right player
    @Override
    public void handleResponse(TextResponse response) {
    }

    //sends the message to the right player
    @Override
    public void handleResponse(TurnStartResponse response) {

    }

}
