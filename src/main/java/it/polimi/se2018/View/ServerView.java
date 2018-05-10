package it.polimi.se2018.View;

import it.polimi.se2018.Network.Connections.RemoteView;
import it.polimi.se2018.Network.Connections.ServerConnection;
import it.polimi.se2018.Network.Messages.Requests.Message;
import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Network.Messages.Responses.*;
import it.polimi.se2018.Utils.Observable;
import it.polimi.se2018.Utils.Observer;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public class ServerView extends Observable<Message> implements Observer<Response>, RemoteView {
    private Map<Player,ServerConnection> playerConnections;

    public ServerView(Map<Player,ServerConnection> playerConnections) {
        this.playerConnections = playerConnections;
    }

    public ServerConnection getPlayerConnection(Player player) {
        return playerConnections.get(player);
    }

    //receives input from the network, notifies the controller
    @Override
    public void handleNetworkInput(Message input) {
        notify(input);
    }

    @Override
    public void handleNetworkInput(Response response) {
        //not implemented serverside
    }

    @Override
    public void update(Response response) {
        handleNetworkOutput(response);
    }

    private void handleNetworkOutput(Response response){
        try {
            if (response instanceof ModelViewResponse) {
                for(ServerConnection connection : playerConnections.values()) {
                    connection.sendResponse(response);
                }
            }
            else playerConnections.get(response.getPlayer()).sendResponse(response);
        }
        catch (RemoteException e) {
            //implement
        }
    }




}
