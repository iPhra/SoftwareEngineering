package it.polimi.se2018.network.connections.rmi;

import it.polimi.se2018.network.connections.ServerConnection;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.mvc.view.ServerView;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIServerConnection implements ServerConnection, RemoteConnection, Runnable {
    private ServerView serverView;
    private final RemoteConnection clientConnection;
    private final List<Message> events;
    private boolean isOpen;
    private final RMIManager manager;
    private final int playerID;


    public RMIServerConnection(RemoteConnection clientConnection, RMIManager manager, int playerID) {
        super();
        events = new ArrayList<>();
        this.clientConnection = clientConnection;
        isOpen = true;
        this.manager = manager;
        this.playerID = playerID;
    }

    @Override
    public void sendResponse(Response response) {
        try {
            clientConnection.getResponse(response);
        }
        catch(RemoteException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

    @Override
    public void setServerView(ServerView serverView) {
        this.serverView = serverView;
    }

    @Override
    public void updateView() {
        synchronized (events) {
            while (events.isEmpty()) {
                try {
                    events.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        serverView.handleNetworkInput(events.remove(0));
    }

    @Override
    public void getResponse(Response response) {
        //not implemented server-side
    }

    @Override
    public void getMessage(Message message) {
        synchronized (events) {
            events.add(message);
            events.notifyAll();
        }
    }

    @Override
    public void stop() {
        isOpen=false;
    }

    @Override
    public void run() {
        while(isOpen) {
            updateView();
        }
        manager.closePlayerConnection(playerID,this);
    }
}
