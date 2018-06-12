package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.Client;
import it.polimi.se2018.client.network.ClientConnection;
import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.sync.SyncResponse;
import it.polimi.se2018.utils.Observable;

import java.util.ArrayList;
import java.util.List;

public class GUIView extends Observable<SyncResponse> implements ClientView {
    private final Client client;
    private final GUIController guiController;
    private final GUIModel guiModel;
    private ClientConnection clientConnection;
    private final List<SyncResponse> events;
    private boolean isOpen;
    private boolean stopAction;

    public GUIView(Client client, int playerID) {
        this.client = client;
        guiModel = new GUIModel(this,playerID);
        guiController = new GUIController(this,guiModel,playerID);
        events = new ArrayList<>();
        isOpen = true;
        stopAction = false;
    }

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

    void handleNetworkOutput(Message message) {
        clientConnection.sendMessage(message);
        wakeUp();
    }

    void endGame() {
        stop();
        client.startNewGame();
    }

    public void setStopAction(boolean stopAction) {
        this.stopAction = stopAction;
    }

    public void start() {
        while(isOpen) {
            wakeUp();
        }
    }

    public GUIController getGuiController() {
        return guiController;
    }

    public GUIModel getGuiModel() {
        return guiModel;
    }

    @Override
    public void handleNetworkInput(SyncResponse syncResponse) {
        synchronized (events) {
            events.add(syncResponse);
            events.notifyAll();
        }
    }

    @Override
    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    @Override
    public void stop() {
        isOpen=false;
        stopAction = false;
    }

    @Override
    public void handleAsyncEvent(boolean halt, String message) {
        if(halt) guiController.halt("");
    }
}
