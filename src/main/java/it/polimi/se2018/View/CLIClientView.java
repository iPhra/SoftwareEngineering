package it.polimi.se2018.View;

import it.polimi.se2018.Connections.ClientConnection;
import it.polimi.se2018.Model.ModelView;
import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Utils.Observer;

import java.io.InputStream;
import java.io.OutputStream;

public class CLIClientView {
    private final Player player;
    private final ClientConnection connection;
    private final InputStream input;
    private final OutputStream output;

    private class NetworkObserver   { //RISOLVERE IL PROBLEMA DEL DOPPIO OSSERVATORE
    }

    public CLIClientView(Player player, ClientConnection connection, InputStream input, OutputStream output) {
        this.player = player;
        this.connection = connection;
        this.input = input;
        this.output = output;
    }

    public void handleError(String error) {}

    //called whenever it's this player's turn
    public void getInput() {}

    public void printModel(ModelView modelView) {}

}
