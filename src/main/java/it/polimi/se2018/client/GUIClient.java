package it.polimi.se2018.client;

import it.polimi.se2018.client.network.ClientConnection;
import it.polimi.se2018.client.network.RMIClientConnection;
import it.polimi.se2018.client.network.SocketClientConnection;
import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.network.connections.rmi.RemoteConnection;
import it.polimi.se2018.network.connections.rmi.RemoteManager;
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
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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
    private RemoteManager manager;

    public GUIClient() {
        setup = true;
    }

    public boolean isSocket() {
        return isSocket;
    }

    public boolean getPlayerName(String playerName){
        if(isSocket) return getPlayerNameSocket(playerName);
        else return getPlayerNameRMI(playerName);
    }

    private boolean getPlayerNameSocket(String playerName){
        if (setup) {
            this.playerName = playerName;
            try{
                out.writeObject(playerName);
                setup = (boolean) in.readObject();
                if (!setup) {
                    playerID = (int) in.readObject();
                    guiClientView = new GUIClientView(playerID);
                    clientConnection = new SocketClientConnection(this,guiClientView, socket,in,out);
                    guiClientView.setClientConnection(clientConnection);
                    new Thread((SocketClientConnection) clientConnection).start();
                }
            }catch(IOException | ClassNotFoundException  e){
                Logger logger = Logger.getAnonymousLogger();
                logger.log(Level.ALL,e.getMessage());
            }
        }
        return setup;
    }

    private boolean getPlayerNameRMI(String playerName){
        if (setup){
            this.playerName = playerName;
            try{
                setup = manager.checkName(playerName);
                if(!setup) {
                    playerID = manager.getID(playerName);
                    guiClientView = new GUIClientView(playerID);
                    clientConnection = new RMIClientConnection(this,guiClientView);
                    manager.addClient(playerID, playerName, (RemoteConnection) UnicastRemoteObject.exportObject((RemoteConnection) clientConnection, 0));
                    RemoteConnection serverConnection = (RemoteConnection) Naming.lookup("//localhost/ServerConnection" + playerID);
                    ((RMIClientConnection) clientConnection).setServerConnection(serverConnection);
                    guiClientView.setClientConnection(clientConnection);
                    new Thread((RMIClientConnection) clientConnection).start();
                    //guiClientView.start();  this is because of timers i guess
                }
            }catch(RemoteException | NotBoundException | MalformedURLException e){
                System.exit(1);
            }
        }
        return setup;
    }

    public ClientView getGUIClientView() {
        return guiClientView;
    }

    public void createRMIConnection(){
        try {
            manager = (RemoteManager) Naming.lookup("//localhost/RemoteManager");
        }catch(RemoteException | NotBoundException | MalformedURLException e){
            System.exit(1);
        }
    }

    public void createSocketConnection(){
        try{
            socket = new Socket(HOST, PORT);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            isSocket = true;
        } catch(IOException e){
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
        startingSceneController.setGuiClient(guiClient);
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