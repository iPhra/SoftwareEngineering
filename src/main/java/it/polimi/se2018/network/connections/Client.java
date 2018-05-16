package it.polimi.se2018.network.connections;

import it.polimi.se2018.network.connections.rmi.RMIClientConnection;
import it.polimi.se2018.network.connections.rmi.RemoteManager;
import it.polimi.se2018.network.connections.rmi.RemoteView;
import it.polimi.se2018.view.ClientView;
import it.polimi.se2018.view.cli.CLIClientView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Client {
    private RemoteView clientView;
    private int playerID;
    private String playerName;
    private Socket socket;
    private boolean setup;
    private final Scanner input;
    private final PrintStream output;

    private Client() {
        setup = true;
        input = new Scanner(System.in);
        output = new PrintStream(System.out);
    }

    private void createRMIConnection() throws RemoteException, NotBoundException, MalformedURLException{
        RemoteManager manager = (RemoteManager) Naming.lookup("//localhost/RemoteManager");
        playerID = manager.getID();
        while(setup) {
            output.println("Choose your nickname");
            playerName = input.next();
            setup = manager.addClient(playerID, playerName, (RemoteView) UnicastRemoteObject.exportObject(clientView,0));
        }
        clientView = new CLIClientView(playerID);
        RemoteView server = (RemoteView) Naming.lookup("//localhost/RemoteView");
        ClientConnection connection = new RMIClientConnection(server);
    }

    private void createSocketConnection(String host, int port){
        try{
            socket = new Socket(host, port);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            try {
                playerID = (int) in.readObject();
                while (setup) {
                    output.println("Choose your nickname");
                    playerName = input.nextLine();
                    out.writeObject(playerName);
                    setup = !(boolean) in.readObject();
                    output.println(setup ? "This nickname is already taken, please choose another one" : "Your nickname is ok");
                }
            }catch (ClassNotFoundException e) {
                System.err.println(e.getMessage());
            }
            SocketClientConnection socketClientConnection = new SocketClientConnection(socket, (ClientView) clientView);
            socketClientConnection.run();
        } catch(IOException e){
            System.err.println(e.getMessage());
        }
    }

    private void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.createSocketConnection("127.0.0.1",1234);
        //client.createRMIConnection();
        //client.createSocketConnection("127.0.0.1",1234);
    }
}
