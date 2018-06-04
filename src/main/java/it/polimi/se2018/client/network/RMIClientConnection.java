package it.polimi.se2018.client.network;

import it.polimi.se2018.client.Client;
import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.network.connections.rmi.RemoteConnection;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.utils.Stopper;
import it.polimi.se2018.utils.WaitingThread;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIClientConnection extends ClientConnection implements RemoteConnection, Runnable, Stopper {
    private final Client client;
    private final List<Response> events;
    private RemoteConnection serverConnection;
    private boolean isOpen;
    private WaitingThread clock;
    private boolean matchPlaying;

    public RMIClientConnection(Client client, ClientView clientView) {
        super(clientView);
        matchPlaying = true;
        this.client = client;
        events = new ArrayList<>();
        isOpen = true;
    }

    private void startTimer() {
        Duration timeout = Duration.ofSeconds(5);
        clock = new WaitingThread(timeout, this);
        clock.start();
    }

    private void pingClient() {
        try {
            serverConnection.ping();
        }
        catch(RemoteException e) {
            if(matchPlaying) {
                client.handleDisconnection();
                clock.interrupt();
            }
        }
        startTimer();
    }

    private void updateView() {
        synchronized (events) {
            while (events.isEmpty()) {
                try {
                    events.wait();
                } catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            }
        }
        events.remove(0).handleClass(this);
    }

    public void setServerConnection(RemoteConnection serverConnection) {
        this.serverConnection = serverConnection;
        pingClient();
    }

    @Override
    public void getResponse(Response response) {
        synchronized (events) {
            events.add(response);
            events.notifyAll();
        }
    }

    @Override
    public void ping() {
        //empty method
    }

    @Override
    public void getMessage(Message message) {
        //not implemented client-side
    }

    @Override
    public void sendMessage(Message message){
        try {
            serverConnection.getMessage(message);
        }
        catch(RemoteException e) {
            client.handleDisconnection();
            clock.interrupt();
        }
    }

    @Override
    public void stop() {
        isOpen = false;
        matchPlaying = false;
        clock.interrupt();
        try {
            UnicastRemoteObject.unexportObject(this,true);
        }
        catch (RemoteException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

    @Override
    public void run() {
        while(isOpen) {
            updateView();
        }
    }

    @Override
    public void halt(String message) {
        pingClient();
    }
}
