package it.polimi.se2018.network.connections;

import it.polimi.se2018.network.connections.RMI.RMIClientConnection;
import it.polimi.se2018.network.connections.RMI.RemoteManager;
import it.polimi.se2018.network.connections.RMI.RemoteView;
import it.polimi.se2018.network.messages.requests.PassMessage;
import it.polimi.se2018.view.CLI.CLIClientView;
import it.polimi.se2018.view.ClientView;

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
    private Socket socket;

    private Client() {
    }

    private void createRMIConnection() throws RemoteException, NotBoundException, MalformedURLException{
        RemoteManager manager = (RemoteManager) Naming.lookup("//localhost/RemoteManager");
        playerID = manager.getID();
        clientView = new CLIClientView(playerID);
        manager.addClient(playerID, (RemoteView) UnicastRemoteObject.exportObject(clientView,0));
        RemoteView server = (RemoteView) Naming.lookup("//localhost/RemoteView");
        server.handleNetworkInput(new PassMessage(playerID));
        ClientConnection connection = new RMIClientConnection(server);
    }

    public void createSocketConnection(String host, int port){
        try{
            socket = new Socket(host,port);
            SocketClientConnection socketClientConnection = new SocketClientConnection(socket, (ClientView) clientView);
            socketClientConnection.run();
        }catch(IOException e){
            System.err.println(e.getMessage());
        }

    }

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException{
        Client client = new Client();
        //client.createRMIConnection();
        client.createSocketConnection("127.0.0.1",1234);

    }
}
