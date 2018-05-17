package it.polimi.se2018.network.connections.rmi;

import it.polimi.se2018.network.connections.Server;
import it.polimi.se2018.network.connections.ServerConnection;

public class RMIManager implements RemoteManager {
    private Server server;

    public RMIManager(Server server) {
        this.server = server;
    }

    public boolean checkName(int playerID, String playerName) {
        return !server.checkName(playerID,playerName);
    }

    public void addClient(int playerID, String playerName, RemoteView clientView) {
        ServerConnection connection = new RMIServerConnection(clientView);
        server.setPlayer(playerID,playerName,connection);
    }

    public int getID() {
        return server.generateID();
    }
}
