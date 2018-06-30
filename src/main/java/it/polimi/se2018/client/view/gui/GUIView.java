package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.Client;
import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.network.messages.requests.Message;
import javafx.application.Platform;

public class GUIView extends ClientView {
    private final GUILogic guiLogic;

    public GUIView(Client client, int playerID) {
        super(client);
        GUIData guiModel = new GUIData(playerID);
        guiLogic = new GUILogic(this, guiModel,playerID);
        register(guiLogic);
    }

    public GUILogic getGuiLogic() {
        return guiLogic;
    }

    @Override
    public void handleNetworkOutput(Message message) {
        clientConnection.sendMessage(message);
    }

    @Override
    public void handleAsyncEvent(boolean halt, String message) {
        if(guiLogic.isGameStarted()) {
            Platform.runLater(() -> guiLogic.refreshText(message));
        }
    }

    @Override
    public void endGame() {
        stop();
    }
}
