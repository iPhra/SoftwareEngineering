package it.polimi.se2018.network.connections.rmi;

import it.polimi.se2018.network.connections.Server;
import it.polimi.se2018.network.connections.ServerConnection;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIManager implements RemoteManager {
    private Server server;
    private Registry registry;

    public RMIManager(Server server, Registry registry) {
        this.server = server;
        this.registry = registry;
    }

    public boolean checkName(int playerID, String playerName) {
        return server.checkName(playerID,playerName);
    }

    public void addClient(int playerID, String playerName, RemoteConnection clientConnection) {
        ServerConnection serverConnection = new RMIServerConnection(clientConnection);
        try {
            registry.rebind("ServerConnection", UnicastRemoteObject.exportObject((RemoteConnection) serverConnection,0));
        }
        catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
        new Thread(((RMIServerConnection) serverConnection)).start();
        server.setPlayer(playerID,playerName,serverConnection);
    }

    public int getID() {
        return server.generateID();
    }
}
