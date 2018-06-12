package it.polimi.se2018.client.view;

import it.polimi.se2018.client.Client;
import it.polimi.se2018.client.network.ClientConnection;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.sync.SyncResponse;
import it.polimi.se2018.utils.Observable;

import java.util.ArrayList;
import java.util.List;

public abstract class ClientView extends Observable<SyncResponse> implements Runnable {
    protected final Client client;
    protected boolean isIterating;
    private boolean isOpen;
    private final List<SyncResponse> events;
    private ClientConnection clientConnection;

    protected ClientView(Client client) {
        this.client = client;
        isOpen = true;
        events = new ArrayList<>();
    }

    public abstract void handleAsyncEvent(boolean halt, String message);

    private void wakeUp() {
        synchronized (events) {
            while (events.isEmpty()) {
                try {
                    events.wait();
                } catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            }
            SyncResponse syncResponse = events.remove(0);
            notify(syncResponse);
        }
    }

    public void endGame() {
        stop();
        client.setGameEnded();
    }

    public void handleNetworkInput(SyncResponse syncResponse) {
        synchronized (events) {
            events.add(syncResponse);
            events.notifyAll();
        }
    }

    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }
    public void stop() {
        isOpen=false;
    }

    public void handleNetworkOutput(Message message) {
        clientConnection.sendMessage(message);
        wakeUp();
    }

    public boolean isIterating() {
        return isIterating;
    }

    @Override
    public void run() {
        while(isOpen) {
            wakeUp();
        }
    }
}
