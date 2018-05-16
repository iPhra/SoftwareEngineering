package it.polimi.se2018.network.connections;

import it.polimi.se2018.network.connections.rmi.RMIClientConnection;
import it.polimi.se2018.network.connections.rmi.RemoteManager;
import it.polimi.se2018.network.connections.rmi.RemoteView;
import it.polimi.se2018.view.ClientView;
import it.polimi.se2018.view.cli.CLIClientView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client {
    private RemoteView clientView;
    private int playerID;
    private String playerName;
    private Socket socket;

    private Client() {
    }

    private void createRMIConnection() throws RemoteException, NotBoundException, MalformedURLException{
        RemoteManager manager = (RemoteManager) Naming.lookup("//localhost/RemoteManager");
        playerID = manager.getID();
        clientView = new CLIClientView(playerID);
        manager.addClient(playerID, playerName, (RemoteView) UnicastRemoteObject.exportObject(clientView,0));
        RemoteView server = (RemoteView) Naming.lookup("//localhost/RemoteView");
        ClientConnection connection = new RMIClientConnection(server);
    }

    private void createSocketConnection(String host, int port){
        try{
            socket = new Socket(host,port);
            SocketClientConnection socketClientConnection = new SocketClientConnection(socket, (ClientView) clientView);
            socketClientConnection.run();
        } catch(IOException e){
        }

    }

    public static void main(String[] args) {
        Client client = new Client();
        client.createSocketConnection("127.0.0.1",1234);
    }
}
