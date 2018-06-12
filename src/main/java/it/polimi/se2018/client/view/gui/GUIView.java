package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.Client;
import it.polimi.se2018.client.network.ClientConnection;
import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.sync.SyncResponse;

import java.util.ArrayList;
import java.util.List;

public class GUIView extends ClientView {
    private final GUIController guiController;
    private final GUIModel guiModel;

    public GUIView(Client client, int playerID) {
        super(client);
        guiModel = new GUIModel(this,playerID);
        guiController = new GUIController(this,guiModel,playerID);
    }

    public GUIController getGuiController() {
        return guiController;
    }

    public GUIModel getGuiModel() {
        return guiModel;
    }

    @Override
    public void handleAsyncEvent(boolean halt, String message) {
        if(halt) {
            //implementare
        }
    }
}
