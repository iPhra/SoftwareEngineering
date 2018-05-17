package it.polimi.se2018.network.connections;

import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.view.ServerView;

import java.rmi.RemoteException;

public interface ServerConnection {

    void sendResponse(Response response) throws RemoteException;

    void setServerView(ServerView serverView);
}
