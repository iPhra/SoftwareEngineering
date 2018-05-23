package it.polimi.se2018.network.connections.rmi;

import it.polimi.se2018.network.Server;
import it.polimi.se2018.network.connections.ServerConnection;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIManager implements RemoteManager {
    private Server server;
    private Registry registry;
    private int playerID;

    public RMIManager(Server server, Registry registry) {
        this.server = server;
        this.registry = registry;
    }

    public boolean checkName(int playerID, String playerName) {
        return server.checkName(playerID,playerName);
    }

    public void addClient(int playerID, String playerName, RemoteConnection clientConnection) {
        ServerConnection serverConnection = new RMIServerConnection(clientConnection, this, playerID);
        try {
            registry.rebind("ServerConnection"+playerID, UnicastRemoteObject.exportObject((RemoteConnection) serverConnection,0));
        }
        catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
        Thread thread = new Thread((RMIServerConnection)serverConnection);
        thread.start();
        server.setPlayer(playerID,playerName,serverConnection);
    }

    public int getID() {
        playerID = server.generateID();
        return playerID;
    }

    public synchronized void closePlayerConnection(int playerID, ServerConnection serverConnection) {
        try {
            registry.unbind("ServerConnection"+playerID);
            UnicastRemoteObject.unexportObject((RemoteConnection)serverConnection,true);
        }
        catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            registry.unbind("RemoteManager");
            UnicastRemoteObject.unexportObject(this, true);
            UnicastRemoteObject.unexportObject(registry, true);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
