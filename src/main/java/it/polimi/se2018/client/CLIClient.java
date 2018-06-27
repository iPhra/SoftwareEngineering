package it.polimi.se2018.client;

import it.polimi.se2018.client.network.RMIClientConnection;
import it.polimi.se2018.client.network.SocketClientConnection;
import it.polimi.se2018.client.view.cli.CLIView;
import it.polimi.se2018.network.connections.rmi.RemoteConnection;
import it.polimi.se2018.network.connections.rmi.RemoteManager;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CLIClient extends Client {
    private CLIView clientView;
    private Socket socket;
    private boolean disconnected;
    private boolean gameEnded;
    private final Scanner input;
    private final PrintStream output;

    public CLIClient() {
        super();
        disconnected = false;
        gameEnded = false;
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

    private synchronized void waitForAction() {
        while(!disconnected && !gameEnded) {
            try {
                this.wait();
            } catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
        if(gameEnded) {
            gameEnded = false;
            startNewGame();
        }
        else {
            disconnected = false;
            handleDisconnection();
        }
    }

    private String getNickname() {
        output.println("\nChoose your nickname");
        return input.nextLine();
    }

    private void getSettings() {
        boolean settings = true;
        do {
            try {
                output.println("\nType [1] to use default settings, [2] to change configs");
                int value = input.nextInt();
                switch (value) {
                    case 1:
                        settings = false;
                        input.nextLine();
                        setDefaultParams();
                        break;
                    case 2:
                        settings = false;
                        input.nextLine();
                        getDifferentParams();
                        break;
                    default: throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                output.println("\nInput is invalid \n");
                input.nextLine();
            }
        }
        while(settings);
    }

    private void getDifferentParams() {
        output.println("\nInsert server's IP");
        ip = input.nextLine();
        output.println("\nInsert server's Port");
        try {
            port = input.nextInt();
            input.nextLine();
        }
        catch(InputMismatchException e) {
            output.println("\nPort is invalid\n");
            input.nextLine();
        }
    }

    private void chooseConnection() {
        boolean connection = true;
        output.println("\nWhat type of connection do you want to use?");
        do {
            try {
                output.println("\nType 1 for Socket, 2 for RMI");
                int value = input.nextInt();
                switch (value) {
                    case 1:
                        connection = false;
                        isSocket = true;
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
                output.println("\nInput is invalid \n");
                input.nextLine();
            }
        }
        while(connection);
        waitForAction();
    }

    private void createRMIConnection() {
        try {
            getSettings();
            RemoteManager manager = (RemoteManager) Naming.lookup("//"+ ip +":"+port+"/RemoteManager");
            while (setup) {
                nickname = getNickname();
                setup = manager.checkName(nickname);
                if(!setup) {
                    output.println("\nYour nickname is ok");
                    playerID = manager.getID(nickname);
                }
                else {
                    output.println("\nThis nickname is too long or already taken, choose another one");
                }
            }
            clientView = new CLIView(this,playerID);
            clientConnection = new RMIClientConnection(this,clientView);
            manager.addClient(playerID, nickname, (RemoteConnection) UnicastRemoteObject.exportObject((RemoteConnection) clientConnection, 0));
            RemoteConnection serverConnection = (RemoteConnection) Naming.lookup("//"+ ip +":"+port+"/ServerConnection" + playerID);
            ((RMIClientConnection) clientConnection).setServerConnection(serverConnection);
        }
        catch(RemoteException | NotBoundException | MalformedURLException e) {
            System.exit(1);
        }
        clientView.setClientConnection(clientConnection);
        new Thread((RMIClientConnection) clientConnection).start();
        Thread thread = new Thread(clientView);
        thread.start();
    }

    private void createSocketConnection(){
        try {
            getSettings();
            socket = new Socket(ip, port);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            while (setup) {
                nickname = getNickname();
                out.writeObject(nickname);
                setup = (boolean) in.readObject();
                if(!setup) {
                    output.println("\nYour nickname is ok");
                    playerID = (int) in.readObject();
                }
                else {
                    output.println("\nThis nickname is too long or already taken, choose another one");
                }
            }
            clientView = new CLIView(this,playerID);
            clientConnection = new SocketClientConnection(this, clientView, socket, in, out);
        }
        catch(IOException | ClassNotFoundException e) {
            System.exit(1);
        }
        clientView.setClientConnection(clientConnection);
        new Thread((SocketClientConnection) clientConnection).start();
        Thread thread = new Thread(clientView);
        thread.start();
    }

    private void handleDisconnection() {
        output.println("\nDisconnected, trying to reconnect..\n");
        clientConnection.stop();
        clientView.stop();
        chooseConnection();
    }

    private void startNewGame() {
        output.println("\nInsert [1] to start another game, anything else to quit");
        int choice = input.nextInt();
        if(choice==1) chooseConnection();
        else System.exit(0);
    }

    @Override
    public synchronized void setDisconnected() {
        disconnected = true;
        setup = true;
        this.notifyAll();
    }

    @Override
    public synchronized void setGameEnded() {
        gameEnded = true;
        setup = true;
        this.notifyAll();
    }






    public static void main(String[] args) {
        CLIClient client = new CLIClient();
        client.welcome();
        client.chooseConnection();
    }
}
