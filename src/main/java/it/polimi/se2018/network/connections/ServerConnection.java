package it.polimi.se2018.network.connections;

import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.mvc.view.ServerView;


public interface ServerConnection {

    void sendResponse(Response response);
    void setServerView(ServerView serverView);
    void stop();
    void setDisconnected();
    void setReconnected();
    boolean isDisconnected();
}
