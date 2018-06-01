package it.polimi.se2018.client;

import it.polimi.se2018.client.network.ClientConnection;
import it.polimi.se2018.client.network.RMIClientConnection;
import it.polimi.se2018.client.network.SocketClientConnection;
import it.polimi.se2018.client.view.cli.CLIView;
import it.polimi.se2018.network.connections.rmi.RemoteConnection;
import it.polimi.se2018.network.connections.rmi.RemoteManager;
import it.polimi.se2018.client.view.ClientView;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.InputMismatchException;
import java.util.Scanner;

class Client {
    private static final int PORT = 1234;
    private static final String HOST = "127.0.0.1";
    private ClientView clientView;
    private ClientConnection clientConnection;
    private int playerID;
    private String nickname;
    private Socket socket;
    private boolean setup;
    private final Scanner input;
    private final PrintStream output;

    private Client() {
        setup = true;
        input = new Scanner(System.in);
        output = new PrintStream(System.out);
    }

    private String getNickname() {
        output.println("Choose your nickname");
        return input.nextLine();
    }

    private void chooseConnection() {
        boolean connection = true;
        output.println("What type of connection do you want to use?");
        do {
            try {
                output.println("Type 1 for Socket, 2 for RMI");
                int value = input.nextInt();
                switch (value) {
                    case 1:
                        connection = false;
                        input.nextLine();
                        createSocketConnection();
                        break;
                    case 2:
                        connection = false;
                        input.nextLine();
                        createRMIConnection();
                        break;
                    default: throw new InputMismatchException();
                }
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
            while (setup) {
                nickname = getNickname();
                setup = manager.checkName(nickname);
                if(!setup) {
                    output.println("Your nickname is ok");
                    playerID = manager.getID(nickname);
                }
                else {
                    output.println("This nickname is already taken, please choose another one");
                }
            }
            clientView = new CLIView(playerID);
            clientConnection = new RMIClientConnection(clientView);
            manager.addClient(playerID, nickname, (RemoteConnection) UnicastRemoteObject.exportObject((RemoteConnection) clientConnection, 0));
            RemoteConnection serverConnection = (RemoteConnection) Naming.lookup("//localhost/ServerConnection" + playerID);
            ((RMIClientConnection) clientConnection).setServerConnection(serverConnection);
        }
        catch(RemoteException | NotBoundException | MalformedURLException e) {
            System.exit(1);
        }
        clientView.setClientConnection(clientConnection);
        new Thread((RMIClientConnection) clientConnection).start();
        ((CLIView) clientView).start();
    }

    private void createSocketConnection(){
        try {
            socket = new Socket(HOST, PORT);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            while (setup) {
                nickname = getNickname();
                out.writeObject(nickname);
                setup = (boolean) in.readObject();
                if(!setup) {
                    output.println("Your nickname is ok");
                    playerID = (int) in.readObject();
                }
                else {
                    output.println("This nickname is already taken, please choose another one");
                }
            }
            clientView = new CLIView(playerID);
            clientConnection = new SocketClientConnection(clientView, socket, in, out);
        }
        catch(IOException | ClassNotFoundException e) {
            System.exit(1);
        }
        clientView.setClientConnection(clientConnection);
        new Thread((SocketClientConnection) clientConnection).start();
        ((CLIView) clientView).start();
    }



    public static void main(String[] args) {
        Client client = new Client();
        client.chooseConnection();
    }
}
