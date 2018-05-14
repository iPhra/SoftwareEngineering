package it.polimi.se2018.network.connections;

import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.requests.MessageHandler;
import it.polimi.se2018.view.ClientView;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    ClientView clientView;

    public void createRMIConnection() throws RemoteException, NotBoundException, MalformedURLException{
        RemoteView server = (RemoteView) Naming.lookup("//localhost/ServerView");
        ClientConnection connection = new RMIClientConnection(server);
        connection.sendMessage(new Message(0) {
            @Override
            public void handle(MessageHandler handler) {
            }
        });
        //settare connection alla view del client
    }

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException{
        Client client = new Client();
        client.createRMIConnection();
    }

}
