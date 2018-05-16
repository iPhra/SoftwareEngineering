package it.polimi.se2018.view;

import it.polimi.se2018.network.connections.rmi.RemoteView;
import it.polimi.se2018.network.connections.ServerConnection;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.*;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;


import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class ServerView extends Observable<Message> implements Observer<Response>, RemoteView{
    private Map<Integer,ServerConnection> playerConnections;

    public ServerView() {
        playerConnections = new HashMap<>();
    }

    public ServerConnection getPlayerConnection(Integer playerID) {
        return playerConnections.get(playerID);
    }

    public void setServerConnections(Integer playerID, ServerConnection serverConnection) {
        playerConnections.put(playerID, serverConnection);
    }

    //receives input from the network, notifies the controller
    @Override
    public void handleNetworkInput(Message message) {
        notify(message);
    }

    @Override
    public void handleNetworkInput(Response response)  {
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
