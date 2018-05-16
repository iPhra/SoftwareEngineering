package it.polimi.se2018.network.connections.rmi;

import it.polimi.se2018.network.connections.Server;
import it.polimi.se2018.view.ServerView;

public class RMIManager implements RemoteManager {
    private ServerView serverView;
    private Server server;

    public RMIManager(Server server, ServerView serverView) {
        this.server = server;
        this.serverView = serverView;

    }

    public void addClient(int playerID, String playerName, RemoteView clientView) {
        serverView.setServerConnection(playerID,new RMIServerConnection(clientView));
        //setta la connessione al server
        //dai playerName a qualcuno
    }

    public int getID() {
        Server.incrementUniqueID();
        return Server.getUniqueID();
    }
}
