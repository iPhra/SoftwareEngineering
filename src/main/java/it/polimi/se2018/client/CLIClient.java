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

public class CLIClient implements Client {
    private int port;
    private String ip;
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
        output.println("\nChoose your nickname");
        return input.nextLine();
    }

    private void getSettings(boolean isSocket) {
        boolean settings = true;
        do {
            try {
                output.println("\nType [1] to use default settings, [2] to change configs");
                int value = input.nextInt();
                switch (value) {
                    case 1:
                        settings = false;
                        input.nextLine();
                        getDefaultParams(isSocket);
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
        output.println("\nInsert server's IP\n");
        ip = input.nextLine();
        output.println("\nInsert server's Port\n");
        try {
            port = input.nextInt();
        }
        catch(InputMismatchException e) {
            output.println("\nPort is invalid\n");
            input.nextLine();
        }
    }

    private void getDefaultParams(boolean isSocket) {
        try(BufferedReader br = new BufferedReader(new FileReader("resources/NetworkProperties.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String[] tokens = sb.toString().split(";");
            ip = tokens[0].split(":")[1];
            port = Integer.valueOf((isSocket? tokens[2].split(":")[1] : tokens[1].split(":")[1]));
        }
        catch (IOException e) {
            System.exit(1);
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
    }

    private void createRMIConnection() {
        try {
            getSettings(false);
            RemoteManager manager = (RemoteManager) Naming.lookup("//"+ ip +":"+port+"/RemoteManager");
            while (setup) {
                nickname = getNickname();
                setup = manager.checkName(nickname);
                if(!setup) {
                    output.println("\nYour nickname is ok");
                    playerID = manager.getID(nickname);
                }
                else {
                    output.println("\nThis nickname is already take , please choose another one");
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
        ((CLIView) clientView).start();
    }

    private void createSocketConnection(){
        try {
            getSettings(true);
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
                    output.println("\nThis nickname is already taken, please choose another one");
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
        ((CLIView) clientView).start();
    }

    @Override
    public void handleDisconnection() {
        output.println("\nDisconnected, trying to reconnect..\n");
        clientConnection.stop();
        setup = true;
        chooseConnection();
    }

    @Override
    public void startNewGame() {
        setup = true;
        output.println("\nInsert [1] to start another game, anything else to quit");
        int choice = input.nextInt();
        if(choice==1) chooseConnection();
        else System.exit(0);
    }


    public static void main(String[] args) {
        CLIClient client = new CLIClient();
        client.welcome();
        client.chooseConnection();
    }
}
