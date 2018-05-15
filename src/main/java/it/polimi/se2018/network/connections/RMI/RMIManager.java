package it.polimi.se2018.network.connections.RMI;

import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.network.messages.responses.ResponseHandler;
import it.polimi.se2018.network.messages.responses.TextResponse;
import it.polimi.se2018.view.ClientView;
import it.polimi.se2018.view.ServerView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class RMIManager implements RemoteManager {
    private ServerView serverView;

    public RMIManager(ServerView serverView) {
        this.serverView = serverView;
    }

    public void addClient(int playerID, RemoteView clientView) {
        serverView.setServerConnection(playerID,new RMIServerConnection(clientView));
        /*try {
            clientView.handleNetworkInput(new TextResponse(playerID,"prova"));
        }
        catch (RemoteException e) {
        }*/
    }

    public int getID() {
        return new Random().nextInt();
    }
}
