package it.polimi.se2018.client.view;

import it.polimi.se2018.client.network.ClientConnection;
import it.polimi.se2018.network.messages.responses.sync.SyncResponse;

public interface ClientView {

    void handleNetworkInput(SyncResponse syncResponse);
    void setClientConnection(ClientConnection clientConnection);
}
