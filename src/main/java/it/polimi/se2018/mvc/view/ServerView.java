package it.polimi.se2018.mvc.view;

import it.polimi.se2018.network.connections.ServerConnection;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.*;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;


import java.util.HashMap;
import java.util.Map;

public class ServerView extends Observable<Message> implements Observer<Response>{
    private final Map<Integer,ServerConnection> playerConnections;

    public ServerView() {
        playerConnections = new HashMap<>();
    }

    public void handleNetworkOutput(Response response) {
        if (response instanceof ModelViewResponse) {
            for (ServerConnection connection : playerConnections.values()) {
                connection.sendResponse(response);
            }
        } else playerConnections.get(response.getPlayer()).sendResponse(response);
    }

    public void removePlayerConnection(int playerID) {
        playerConnections.remove(playerID);
    }

    public void addServerConnection(int playerID, ServerConnection serverConnection) {
        playerConnections.put(playerID, serverConnection);
    }

    //receives input from the network, notifies the controller
    public void handleNetworkInput(Message message) {
        notify(message);
    }

    @Override
    public void update(Response response) {
        handleNetworkOutput(response);
    }
}
