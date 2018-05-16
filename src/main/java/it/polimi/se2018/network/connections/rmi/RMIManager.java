package it.polimi.se2018.network.connections.rmi;

import it.polimi.se2018.network.connections.Server;
import it.polimi.se2018.network.connections.ServerConnection;
import it.polimi.se2018.view.ServerView;

public class RMIManager implements RemoteManager {
    private ServerView serverView;
    private Server server;

    public RMIManager(Server server, ServerView serverView) {
        this.server = server;
        this.serverView = serverView;

    }

    public boolean addClient(int playerID, String playerName, RemoteView clientView) {
        server.checkName(playerID,playerName);
        ServerConnection connection = new RMIServerConnection(clientView);
        serverView.setServerConnections(playerID,connection);
        server.setPlayer(playerID,playerName,connection);
        return false;
    }

    public int getID() {
        return Server.generateID();
    }
}
