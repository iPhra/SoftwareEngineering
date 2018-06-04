package it.polimi.se2018.client;

import it.polimi.se2018.client.network.ClientConnection;
import it.polimi.se2018.client.network.SocketClientConnection;
import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.client.view.gui.GUIClientView;
import it.polimi.se2018.client.view.gui.StartingSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GUIClient extends Application implements Client{
    private static final int PORT = 1234;
    private static final String HOST = "127.0.0.1";
    private GUIClientView guiClientView;
    private ClientConnection clientConnection;
    private int playerID;
    private String playerName;
    private Socket socket;
    private boolean setup;
    private InputStream playerNameStream;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean isSocket;

    public GUIClient() {
        setup = true;
    }

    public boolean isSocket() {
        return isSocket;
    }

    //has to be implemented
    public boolean getPlayerNameSocket(String playerName){
        if (setup) {
            this.playerName = playerName;
            try{
                out.writeObject(playerName);
                setup = (boolean) in.readObject();
                if (!setup) {
                    guiClientView = new GUIClientView(playerID);
                }
            }catch(IOException | ClassNotFoundException  e){
                Logger logger = Logger.getAnonymousLogger();
                logger.log(Level.ALL,e.getMessage());
            }
        }
        return setup;
    }

    public ClientView getGUIClientView() {
        return guiClientView;
    }

    public void createRMIConnection(){
        /*
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
        */
    }

    public void createSocketConnection(){
        try{
            socket = new Socket(HOST, PORT);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            playerID = (int) in.readObject();
            isSocket = true;
            clientConnection = new SocketClientConnection(this,guiClientView, socket,in,out);
            guiClientView.setClientConnection(clientConnection);
            new Thread((SocketClientConnection) clientConnection).start();
        } catch(IOException | ClassNotFoundException e){
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        GUIClient guiClient = new GUIClient();
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/scenes/startingScene.fxml")));
        Parent root = loader.load();
        StartingSceneController startingSceneController = loader.getController();
        startingSceneController.setGUIClient(guiClient);
        primaryStage.setTitle("Sagrada Online");
        primaryStage.setScene(new Scene(root, 600, 623));
        startingSceneController.setStage(primaryStage);
        primaryStage.show();

    }

    @Override
    public void handleDisconnection() {
        //implement
    }

    @Override
    public void startNewGame() {
        //implement
    }
}