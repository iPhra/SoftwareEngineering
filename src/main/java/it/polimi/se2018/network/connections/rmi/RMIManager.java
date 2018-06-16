package it.polimi.se2018.network.connections.rmi;

import it.polimi.se2018.network.Server;
import it.polimi.se2018.network.connections.ServerConnection;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIManager implements RemoteManager {
    private final Server server;
    private final Registry registry;

    public RMIManager(Server server, Registry registry) {
        this.server = server;
        this.registry = registry;
    }

    //false if he's not registered or reconnecting
    public boolean checkName(String nickname) {
        return nickname.length()>15 || !(server.checkRegistration(nickname) || server.checkDisconnection(nickname));
    }

    public void addClient(int playerID, String nickname, RemoteConnection clientConnection) {
        ServerConnection serverConnection = new RMIServerConnection(server, clientConnection, this, playerID);
        try {
            registry.rebind("ServerConnection"+playerID, UnicastRemoteObject.exportObject((RemoteConnection) serverConnection,0));
        }
        catch (RemoteException e) {
            closeConnection();
        }
        Thread thread = new Thread((RMIServerConnection)serverConnection);
        thread.start();
        if(server.checkRegistration(nickname)) server.setPlayer(playerID,nickname,serverConnection);
        else server.handleReconnection(playerID,serverConnection);
    }

    public int getID(String nickname) {
        return server.checkRegistration(nickname)? Server.generateID() : server.getPlayerID(nickname);
    }

    public synchronized void closePlayerConnection(int playerID, ServerConnection serverConnection) {
        try {
            registry.unbind("ServerConnection"+playerID);
            UnicastRemoteObject.unexportObject((RemoteConnection)serverConnection,true);
        }
        catch (NotBoundException | RemoteException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            registry.unbind("RemoteManager");
            UnicastRemoteObject.unexportObject(this, true);
            UnicastRemoteObject.unexportObject(registry, true);
        }
        catch (RemoteException | NotBoundException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.SEVERE,"error while closing",e);
        }
    }
}
