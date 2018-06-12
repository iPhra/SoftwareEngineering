package it.polimi.se2018.client;

import it.polimi.se2018.client.network.ClientConnection;
import it.polimi.se2018.client.network.RMIClientConnection;
import it.polimi.se2018.client.network.SocketClientConnection;
import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.client.view.gui.GUIView;
import it.polimi.se2018.network.connections.rmi.RemoteConnection;
import it.polimi.se2018.network.connections.rmi.RemoteManager;
import it.polimi.se2018.client.view.gui.StartingSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
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

public class GUIClient extends Client{
    private static final int PORT = 1234;
    private static final String HOST = "127.0.0.1";
    private GUIView clientView;
    private ClientConnection clientConnection;
    private int playerID;
    private String nickname;
    private Socket socket;
    private boolean setup;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean isSocket;
    private RemoteManager manager;


    public GUIClient() {
        setup = true;
    }

    private boolean getPlayerNameSocket(String playerName){
        if (setup) {
            this.nickname = playerName;
            try{
                out.writeObject(playerName);
                setup = (boolean) in.readObject();
                if (!setup) {
                    playerID = (int) in.readObject();
                    clientView = new GUIView(this,playerID);
                    clientConnection = new SocketClientConnection(this, clientView, socket,in,out);
                    clientView.setClientConnection(clientConnection);
                    new Thread((SocketClientConnection) clientConnection).start();
                    new Thread(clientView).start();
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
            this.nickname = playerName;
            try{
                setup = manager.checkName(playerName);
                if(!setup) {
                    playerID = manager.getID(playerName);
                    clientView = new GUIView(this,playerID);
                    clientConnection = new RMIClientConnection(this, clientView);
                    manager.addClient(playerID, playerName, (RemoteConnection) UnicastRemoteObject.exportObject((RemoteConnection) clientConnection, 0));
                    RemoteConnection serverConnection = (RemoteConnection) Naming.lookup("//localhost/ServerConnection" + playerID);
                    ((RMIClientConnection) clientConnection).setServerConnection(serverConnection);
                    clientView.setClientConnection(clientConnection);
                    new Thread((RMIClientConnection) clientConnection).start();
                    new Thread(clientView).start();
                }
            }catch(RemoteException | NotBoundException | MalformedURLException e){
                System.exit(1);
            }
        }
        return setup;
    }

    public boolean isSocket() {
        return isSocket;
    }

    public boolean getPlayerName(String playerName){
        if(isSocket) return getPlayerNameSocket(playerName);
        else return getPlayerNameRMI(playerName);
    }

    public ClientView getGUIView() {
        return clientView;
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
    void startNewGame() {
        setup = false;
        //implementa
    }

    @Override
    void handleDisconnection() {
        //implementa
    }




    public static void main(String[] args) {
        launch(args);
    }
}