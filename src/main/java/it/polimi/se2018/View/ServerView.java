package it.polimi.se2018.View;

import it.polimi.se2018.Connections.ServerConnection;
import it.polimi.se2018.Model.ModelView;
import it.polimi.se2018.Model.Moves.MoveMessage;
import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Utils.Observable;
import it.polimi.se2018.Utils.Observer;


import java.util.Map;

public class ServerView extends Observable<MoveMessage> implements Observer<ModelView>{
    private Map<Player,ServerConnection> playerConnections;

    private class NetworkObserver implements Observer<Object> {
        @Override
        public void update(Object object) {
            if (object instanceof MoveMessage)
            handleInput((MoveMessage) object);
        }
    }

    public ServerView(Map<Player,ServerConnection> playerConnections) {
        this.playerConnections = playerConnections;
    }

    public ServerConnection getPlayerConnection(Player player) {
        return playerConnections.get(player);
    }

    //broadcasts the model to all clients
    public void handleOutput(ModelView output) {
    }

    //receives input from the network, notifies the controller
    public void handleInput(MoveMessage input) {
        notify(input);
    }

    //used to notify errors, turn start or turn end. Called by the controller
    public void messageService(String message, Player player) {}

    //receives an updated version of the model, sends it to the clients
    @Override
    public void update(ModelView model) {
    }

}
