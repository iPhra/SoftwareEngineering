package it.polimi.se2018.network.connections.rmi;

import it.polimi.se2018.network.Server;
import it.polimi.se2018.network.connections.ServerConnection;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.mvc.view.ServerView;
import it.polimi.se2018.utils.Stopper;
import it.polimi.se2018.utils.WaitingThread;

import java.rmi.RemoteException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class RMIServerConnection implements ServerConnection, RemoteConnection, Runnable, Stopper {
    private final Server server;
    private ServerView serverView;
    private final RemoteConnection clientConnection;
    private final List<Message> events;
    private boolean isOpen;
    private final RMIManager manager;
    private final int playerID;
    private WaitingThread clock;

    public RMIServerConnection(Server server, RemoteConnection clientConnection, RMIManager manager, int playerID) {
        super();
        this.server = server;
        events = new ArrayList<>();
        this.clientConnection = clientConnection;
        isOpen = true;
        this.manager = manager;
        this.playerID = playerID;
    }

    private void startTimer() {
        Duration timeout = Duration.ofSeconds(5);
        clock = new WaitingThread(timeout, this);
        clock.start();
    }

    private void updateView() {
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

    private void pingClient() {
        try {
            clientConnection.ping();
            startTimer();
        }
        catch(RemoteException e) {
            server.handleDisconnection(playerID);
            clock.interrupt();
        }
    }

    @Override
    public void sendResponse(Response response) {
        try {
            clientConnection.getResponse(response);
        }
        catch(RemoteException e) {
            if(isOpen) {
                server.handleDisconnection(playerID);
                clock.interrupt();
            }
        }
    }

    @Override
    public void setServerView(ServerView serverView) {
        this.serverView = serverView;
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
    public void ping() {
        //Pinging has to be empty
    }

    @Override
    public void stop() {
        clock.interrupt();
        isOpen=false;
    }

    @Override
    public void run() {
        pingClient();
        while(isOpen) {
            updateView();
        }
        manager.closePlayerConnection(playerID,this);
    }

    @Override
    public void halt(String message) {
        pingClient();
    }
}
