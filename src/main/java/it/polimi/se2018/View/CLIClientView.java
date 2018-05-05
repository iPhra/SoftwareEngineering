package it.polimi.se2018.View;

import it.polimi.se2018.Connections.Connection;
import it.polimi.se2018.Model.ModelView;
import it.polimi.se2018.Model.Player;
import it.polimi.se2018.Utils.Observer;

import java.io.InputStream;
import java.io.OutputStream;

public class CLIClientView {
    private final Player player;
    private final Connection connection;
    private final InputStream input;
    private final OutputStream output;

    private class NetworkObserver implements Observer<Object> {
        @Override
        public void update(Object object) {
            if (object instanceof String) messageService((String)object);
            if (object instanceof ModelView) printModel((ModelView)object);
        }
    }

    public CLIClientView(Player player, Connection connection, InputStream input, OutputStream output) {
        this.player = player;
        this.connection = connection;
        this.input = input;
        this.output = output;
    }

    //if it's my turn i call getInput, else if it's an error i print the error
    public void messageService(String message) {}

    //called by messageService when it receives an error
    public void printMessage(String error) {}

    //called whenever it's this player's turn
    public void getInput() {}

    public void printModel(ModelView modelView) {}

}
