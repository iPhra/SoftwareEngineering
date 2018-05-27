package it.polimi.se2018.network.connections;

import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.mvc.view.ServerView;


public abstract class ServerConnection {
    private boolean disconnected;

    protected ServerConnection() {
        disconnected=false;
    }

    public abstract void sendResponse(Response response);
    public abstract void setServerView(ServerView serverView);
    public abstract void stop();

    public void setDisconnected() {disconnected = true;}

    public synchronized void setReconnected() {
        disconnected = false;
        this.notifyAll();
    }

    protected boolean isDisconnected() {return disconnected;}
}
