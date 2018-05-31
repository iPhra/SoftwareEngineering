package it.polimi.se2018.client.network;

import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.network.connections.rmi.RemoteConnection;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.Response;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class RMIClientConnection extends ClientConnection implements RemoteConnection, Runnable {
    private final List<Response> events;
    private RemoteConnection serverConnection;
    private boolean isOpen;

    public RMIClientConnection(ClientView clientView) {
        super(clientView);
        events = new ArrayList<>();
        isOpen = true;
    }

    public void setServerConnection(RemoteConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    @Override
    public void getResponse(Response response) {
        synchronized (events) {
            events.add(response);
            events.notifyAll();
        }
    }

    @Override
    public void getMessage(Message message) { //not implemented client-side
    }

    @Override
    public void updateView() {
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

    @Override
    public void sendMessage(Message message){
        try {
            serverConnection.getMessage(message);
        }
        catch(RemoteException e) {
            //riconnettiti
        }
    }

    @Override
    public void stop() {
        isOpen = false;
    }

    @Override
    public void run() {
        while(isOpen) {
            updateView();
        }
    }
}
