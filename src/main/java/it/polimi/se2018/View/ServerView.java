package it.polimi.se2018.View;

import it.polimi.se2018.Connections.Connection;
import it.polimi.se2018.Model.ModelView;
import it.polimi.se2018.Model.Messages.Message;
import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Utils.Observable;
import it.polimi.se2018.Utils.Observer;


import java.util.Map;

public class ServerView extends Observable<Message> implements Observer<ModelView>{
    private Map<Player,Connection> playerConnections;

    private class NetworkObserver implements Observer<Message> {
        @Override
        public void update(Message message) {
            handleInput(message);
        }
    }

    public ServerView(Map<Player,Connection> playerConnections) {
        this.playerConnections = playerConnections;
    }

    public Connection getPlayerConnection(Player player) {
        return playerConnections.get(player);
    }

    //broadcasts the model to all clients
    public void handleOutput(ModelView output) {
    }

    //receives input from the network, notifies the controller
    public void handleInput(Message input) {
        notify(input);
    }

    //used to notify errors, turn start or turn end. Called by the controller
    public void messageService(String message, Player player) {}

    //receives an updated version of the model, sends it to the clients
    @Override
    public void update(ModelView model) {
    }

}
