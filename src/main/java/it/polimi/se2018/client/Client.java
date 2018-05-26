package it.polimi.se2018.client;

import it.polimi.se2018.client.network.ClientConnection;
import it.polimi.se2018.client.network.RMIClientConnection;
import it.polimi.se2018.client.network.SocketClientConnection;
import it.polimi.se2018.network.connections.rmi.RemoteConnection;
import it.polimi.se2018.network.connections.rmi.RemoteManager;
import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.client.view.cli.CLIClientView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {
    private static final int PORT = 1234;
    private static final String HOST = "127.0.0.1";
    private ClientView clientView;
    private ClientConnection clientConnection;
    private int playerID;
    private String playerName;
    private Socket socket;
    private boolean setup;
    private final Scanner input;
    private final PrintStream output;
    private String email;

    private Client() {
        setup = true;
        input = new Scanner(System.in);
        output = new PrintStream(System.out);
    }

    private String getPlayerName() {
        output.println("Choose your nickname");
        return input.nextLine();
    }

    private void chooseConnection() {
        //output.println("Give your email");
        //email = input.next();
        boolean connection = true;
        output.println("What type of connection do you want to use?");
        do {
            try {
                output.println("Type 1 for Socket, 2 for RMI");
                int value = input.nextInt();
                if (value == 1) {
                    connection = false;
                    input.nextLine();
                    createSocketConnection();
                }
                else if (value == 2){
                    connection = false;
                    input.nextLine();
                    createRMIConnection();
                }
                else throw new InputMismatchException();
            } catch (InputMismatchException e) {
                output.println("Input is invalid \n");
                input.nextLine();
            }
        }
        while(connection);
    }

    private void createRMIConnection() {
        try {
            RemoteManager manager = (RemoteManager) Naming.lookup("//localhost/RemoteManager");
            playerID = manager.getID();
            while (setup) {
                playerName = getPlayerName();
                setup = manager.checkName(playerID, playerName);
                output.println(setup ? "This nickname is already taken, please choose another one" : "Your nickname is ok");
            }
            clientView = new CLIClientView(playerID);
            clientConnection = new RMIClientConnection(clientView);
            manager.addClient(playerID, playerName, (RemoteConnection) UnicastRemoteObject.exportObject((RemoteConnection) clientConnection, 0));
            RemoteConnection serverConnection = (RemoteConnection) Naming.lookup("//localhost/ServerConnection" + playerID);
            ((RMIClientConnection) clientConnection).setServerConnection(serverConnection);
        }
        catch(RemoteException | NotBoundException | MalformedURLException e) {
            System.exit(1);
        }
        clientView.setClientConnection(clientConnection);
        new Thread((RMIClientConnection) clientConnection).start();
    }

    private void createSocketConnection(){
        try {
            socket = new Socket(HOST, PORT);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            //out.writeObject(email);
            playerID = (int) in.readObject();
            //setup = (boolean) in.readObject();
            while (setup) {
                playerName = getPlayerName();
                out.writeObject(playerName);
                setup = (boolean) in.readObject();
                output.println(setup ? "This nickname is already taken, please choose another one" : "Your nickname is ok");
            }
            clientView = new CLIClientView(playerID);
            clientConnection = new SocketClientConnection(socket, clientView,in,out);
        }
        catch(IOException | ClassNotFoundException e) {
            System.exit(1);
        }
        clientView.setClientConnection(clientConnection);
        new Thread((SocketClientConnection) clientConnection).start();
    }



    public static void main(String[] args) {
        Client client = new Client();
        client.chooseConnection();
    }
}
