package it.polimi.se2018.client.view.cli;

import it.polimi.se2018.client.Client;
import it.polimi.se2018.client.network.ClientConnection;
import it.polimi.se2018.client.network.RMIClientConnection;
import it.polimi.se2018.client.network.SocketClientConnection;
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

public class CLIClient implements Client {
    private int port;
    private String host;
    private ClientView clientView;
    private ClientConnection clientConnection;
    private int playerID;
    private String nickname;
    private Socket socket;
    private boolean setup;
    private final Scanner input;
    private final PrintStream output;

    private CLIClient() {
        setup = true;
        input = new Scanner(System.in);
        output = new PrintStream(System.out);
    }

    private void welcome() {
        output.println(" _  _  _ _______        _______  _____  _______ _______      _______  _____     \n" +
                " |  |  | |______ |      |       |     | |  |  | |______         |    |     |    \n" +
                " |__|__| |______ |_____ |_____  |_____| |  |  | |______         |    |_____| . .\n" +
                "                                                                                ");
        output.println(" ▄▄▄▄▄▄▄▄▄▄▄  ▄▄▄▄▄▄▄▄▄▄▄  ▄▄▄▄▄▄▄▄▄▄▄  ▄▄▄▄▄▄▄▄▄▄▄  ▄▄▄▄▄▄▄▄▄▄▄  ▄▄▄▄▄▄▄▄▄▄   ▄▄▄▄▄▄▄▄▄▄▄ \n" +
                "▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░▌ ▐░░░░░░░░░░░▌\n" +
                "▐░█▀▀▀▀▀▀▀▀▀ ▐░█▀▀▀▀▀▀▀█░▌▐░█▀▀▀▀▀▀▀▀▀ ▐░█▀▀▀▀▀▀▀█░▌▐░█▀▀▀▀▀▀▀█░▌▐░█▀▀▀▀▀▀▀█░▌▐░█▀▀▀▀▀▀▀█░▌\n" +
                "▐░▌          ▐░▌       ▐░▌▐░▌          ▐░▌       ▐░▌▐░▌       ▐░▌▐░▌       ▐░▌▐░▌       ▐░▌\n" +
                "▐░█▄▄▄▄▄▄▄▄▄ ▐░█▄▄▄▄▄▄▄█░▌▐░▌ ▄▄▄▄▄▄▄▄ ▐░█▄▄▄▄▄▄▄█░▌▐░█▄▄▄▄▄▄▄█░▌▐░▌       ▐░▌▐░█▄▄▄▄▄▄▄█░▌\n" +
                "▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░▌▐░░░░░░░░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░▌       ▐░▌▐░░░░░░░░░░░▌\n" +
                " ▀▀▀▀▀▀▀▀▀█░▌▐░█▀▀▀▀▀▀▀█░▌▐░▌ ▀▀▀▀▀▀█░▌▐░█▀▀▀▀█░█▀▀ ▐░█▀▀▀▀▀▀▀█░▌▐░▌       ▐░▌▐░█▀▀▀▀▀▀▀█░▌\n" +
                "          ▐░▌▐░▌       ▐░▌▐░▌       ▐░▌▐░▌     ▐░▌  ▐░▌       ▐░▌▐░▌       ▐░▌▐░▌       ▐░▌\n" +
                " ▄▄▄▄▄▄▄▄▄█░▌▐░▌       ▐░▌▐░█▄▄▄▄▄▄▄█░▌▐░▌      ▐░▌ ▐░▌       ▐░▌▐░█▄▄▄▄▄▄▄█░▌▐░▌       ▐░▌\n" +
                "▐░░░░░░░░░░░▌▐░▌       ▐░▌▐░░░░░░░░░░░▌▐░▌       ▐░▌▐░▌       ▐░▌▐░░░░░░░░░░▌ ▐░▌       ▐░▌\n" +
                " ▀▀▀▀▀▀▀▀▀▀▀  ▀         ▀  ▀▀▀▀▀▀▀▀▀▀▀  ▀         ▀  ▀         ▀  ▀▀▀▀▀▀▀▀▀▀   ▀         ▀ \n" +
                "                                                                                           \n\n\n\n");
    }

    private String getNickname() {
        output.println("Choose your nickname");
        return input.nextLine();
    }

    private void getParams() {
        boolean condition = true;
        do {
            try {
                output.println("\n[1] for default IP/PORT configs [2] to change configs");
                int choice = input.nextInt();
                switch (choice) {
                    case 1:
                        condition = false;
                        input.nextLine();
                        setDefaultConfig();
                        break;
                    case 2:
                        condition = false;
                        input.nextLine();
                        setOtherConfig();
                        break;
                    default:
                        break;
                }
            }
            catch(InputMismatchException e) {
                output.println("Input is invalid \n");
                input.nextLine();
            }
        }
        while(condition);
    }

    private void setDefaultConfig() {
        try {
            host = "127.0.0.1";
            port = 1234;
            socket = new Socket(host, port);
        }
        catch(IOException e) {
            System.exit(1);
        }
    }

    private void setOtherConfig() {
        boolean condition = true;
        do {
            try {
                output.println("Insert IP");
                host = input.nextLine();
                output.println("Insert Port");
                port = input.nextInt();
                socket = new Socket(host, port);
                condition = false;
            }
            catch (InputMismatchException e) {
                output.println("Port is invalid \n");
                input.nextLine();
            }
            catch (IOException e) {
                output.println("Unable to connect to "+host+":"+port);
                input.nextLine();
            }
        }
        while(condition);
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
            getParams();
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
            clientConnection = new SocketClientConnection(this, clientView, socket, in, out);
        }
        catch(IOException | ClassNotFoundException e) {
            System.exit(1);
        }
        clientView.setClientConnection(clientConnection);
        new Thread((SocketClientConnection) clientConnection).start();
        ((CLIView) clientView).start();
    }

    @Override
    public void handleDisconnection() {
        output.println("\nDisconnected, trying to reconnect..\n");
        clientConnection.stop();
        setup = true;
        chooseConnection();
    }



    public static void main(String[] args) {
        CLIClient client = new CLIClient();
        client.welcome();
        client.chooseConnection();
    }
}
