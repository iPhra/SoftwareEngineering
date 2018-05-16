package it.polimi.se2018.network.connections;

import it.polimi.se2018.network.connections.rmi.RMIManager;
import it.polimi.se2018.network.connections.rmi.RemoteManager;
import it.polimi.se2018.network.connections.rmi.RemoteView;
import it.polimi.se2018.view.ServerView;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private RemoteManager remoteManager;
    private RemoteView remoteView;
    private static int matchID = 0;
    private static int playerNumber = 0; //identifies just the player, without the matc
    private Map<Integer,List<Integer>> matches; //maps id of the match to its playerNumber
    private Map<Integer,ServerConnection> serverConnections; //maps playerID to its connection
    private Map<Integer, String> playerNames; //maps playerID to its name
    private ServerView serverView = new ServerView();
    private ServerSocket serverSocket;

    private Server() {
        remoteView = new ServerView();
        remoteManager = new RMIManager(this, (ServerView) remoteView);
        matches = new HashMap<>();
        playerNames = new HashMap<>();
        serverConnections = new HashMap<>();
    }

    public void setPlayer(int playerID, String playerName, ServerConnection serverConnection) {
        serverConnections.put(playerID,serverConnection);
        playerNames.put(playerID,playerName);
        matches.computeIfAbsent(playerID/1000, k ->  new ArrayList<Integer>());
        matches.get(playerID/1000).add(playerID%1000);
    }

    public boolean checkName(int playerID, String playerName) {
        for(Integer player : playerNames.keySet()) {
            if (player/1000==playerID/1000 && playerName.equals(playerNames.get(player))) return false;
        }
        return true;
    }

    private static void incrementMatchID() {matchID++;}

    private static void incrementPlayerID() {playerNumber++;}

    private boolean isMatchFull() {
        return matches.get(matchID)==null || matches.get(matchID).size()==4; //oppure timer è scaduto?
    }

    public int generateID() {
        if (isMatchFull()) incrementMatchID();
        incrementPlayerID();
        return (matchID*1000)+playerNumber;
    }

    private void createRMIRegistry () throws RemoteException{
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("RemoteManager", UnicastRemoteObject.exportObject(remoteManager,0));
        registry.rebind("RemoteView", UnicastRemoteObject.exportObject(remoteView,0));
    }

    private void startSocketConnection(int port){
        try{
            serverSocket = new ServerSocket(port);
            SocketHandler socketHandler = new SocketHandler(this,serverSocket,serverView);
            socketHandler.run();
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }

    private void closeSocketConnection(){
        try{
            serverSocket.close();
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }

    //metodi per creare Tool Cards, Private Objectives, Public Objectives (classe deck?)

    //metodo randevouz

    public static void main(String[] args) throws RemoteException {
        Server server = new Server();
        server.startSocketConnection(1234);
        server.createRMIRegistry();
    }
}
