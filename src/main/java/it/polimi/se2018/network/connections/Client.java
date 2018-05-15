package it.polimi.se2018.network.connections;

import it.polimi.se2018.network.connections.RMI.RMIClientConnection;
import it.polimi.se2018.network.connections.RMI.RemoteManager;
import it.polimi.se2018.network.connections.RMI.RemoteView;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.requests.MessageHandler;
import it.polimi.se2018.network.messages.requests.PassMessage;
import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.network.messages.responses.ResponseHandler;
import it.polimi.se2018.view.CLI.CLIClientView;
import it.polimi.se2018.view.ClientView;
import it.polimi.se2018.view.GUIClientView;
import it.polimi.se2018.view.ServerView;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class Client {
    private RemoteView clientView;
    private int playerID;

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

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException{
        Client client = new Client();
        client.createRMIConnection();
    }
}
