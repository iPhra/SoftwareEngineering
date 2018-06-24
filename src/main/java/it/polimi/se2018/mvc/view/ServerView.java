package it.polimi.se2018.mvc.view;

import it.polimi.se2018.network.connections.ServerConnection;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;


import java.util.HashMap;
import java.util.Map;

/**
 * This is the View server-side, it's one for all players
 */
public class ServerView extends Observable<Message> implements Observer<Response> {

    /**
     * Maps each playerID to its connection
     */
    private final Map<Integer,ServerConnection> playerConnections;

    public ServerView() {
        playerConnections = new HashMap<>();
    }

    /**
     * Sends a response to a player if he's connected to the server
     * @param response is the message to send on the network
     */
    public void handleNetworkOutput(Response response) {
        if (playerConnections.containsKey(response.getPlayer())) playerConnections.get(response.getPlayer()).sendResponse(response);
    }

    /**
     * Removes a Connection from playerConnections when a player disconnects
     * @param playerID is the ID of the player
     */
    public void removePlayerConnection(int playerID) {
        playerConnections.remove(playerID);
    }

    /**
     * Adds a Connection to playerConnections when a player connects/reconnects
     * @param playerID is the ID of the player connecting
     * @param serverConnection is the Connection associated to that player
     */
    public void addServerConnection(int playerID, ServerConnection serverConnection) {
        playerConnections.put(playerID, serverConnection);
    }

    /**
     * Receveis a message from a Client and forwards it to the {@link it.polimi.se2018.mvc.controller.Controller}
     * @param message is the message received
     */
    public void handleNetworkInput(Message message) {
        notify(message);
    }

    /**
     * Receveis a response from the {@link it.polimi.se2018.mvc.model.Board} and forwards it on the network
     * @param response is the response receveid
     */
    @Override
    public void update(Response response) {
        handleNetworkOutput(response);
    }
}
