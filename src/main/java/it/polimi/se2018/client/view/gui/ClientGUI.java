package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.client.network.ClientConnection;
import it.polimi.se2018.client.network.RMIClientConnection;
import it.polimi.se2018.client.network.SocketClientConnection;
import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.network.connections.rmi.RemoteConnection;
import it.polimi.se2018.network.connections.rmi.RemoteManager;
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
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientGUI extends Application{
    private static final int PORT = 1234;
    private static final String HOST = "127.0.0.1";
    private ClientView clientView;
    private ClientConnection clientConnection;
    private int playerID;
    private String playerName;
    private Socket socket;
    private boolean setup;
    private Stage primaryStage;

    public ClientGUI() {
        setup = true;
    }

    private String getPlayerName(){
        return null;
    }

    void createRMIConnection(){
        try {
            RemoteManager manager = (RemoteManager) Naming.lookup("//localhost/RemoteManager");
            playerID = manager.getID();
            while (setup) {
                playerName = getPlayerName();
                setup = manager.checkName(playerID, playerName);
                //here i must write the output that in ClientCLI is:
                //output.println(setup ? "This nickname is already taken, please choose another one" : "Your nickname is ok");
            }
            clientView = new GUIClientView(playerID);
            clientConnection = new RMIClientConnection(clientView);
            manager.addClient(playerID, playerName, (RemoteConnection) UnicastRemoteObject.exportObject((RemoteConnection) clientConnection, 0));
            RemoteConnection serverConnection = (RemoteConnection) Naming.lookup("//localhost/ServerConnection" + playerID);
            ((RMIClientConnection) clientConnection).setServerConnection(serverConnection);
            clientView.setClientConnection(clientConnection);
            new Thread((RMIClientConnection) clientConnection).start();
        }
        catch(RemoteException | NotBoundException | MalformedURLException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }

    }

    void createSocketConnection(){
        try{
            socket = new Socket(HOST, PORT);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            playerID = (int) in.readObject();
            while (setup) {
                playerName = getPlayerName();
                out.writeObject(playerName);
                setup = (boolean) in.readObject();
                //here i must write the output that in ClientCLI is:
                //output.println(setup ? "This nickname is already taken, please choose another one" : "Your nickname is ok");
            }
            clientView = new GUIClientView(playerID);
            clientConnection = new SocketClientConnection(socket, clientView,in,out);
            clientView.setClientConnection(clientConnection);
            new Thread((SocketClientConnection) clientConnection).start();
        } catch(IOException | ClassNotFoundException e){
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void windowPlayerName(){
        createScene("/playerNameScene.fxml","Choose nick", primaryStage, this);

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        ClientGUI clientGUI = new ClientGUI();
        createScene("/startingScene.fxml", "Hello World", primaryStage, clientGUI);
        primaryStage.show();
        this.primaryStage = primaryStage;
        //Scanner in = new Scanner(System.in);
        //String lol = in.nextLine();
        windowPlayerName();
        //TimeUnit.SECONDS.sleep(10);
    }

    private void createScene(String path, String title, Stage stage, ClientGUI clientGUI){
        FXMLLoader loader = new FXMLLoader((getClass().getResource((path))));
        try {
            Parent root = loader.load();
            SceneController sceneController = loader.getController();
            sceneController.setClientGUI(clientGUI);
            stage.setTitle(title);
            stage.setScene(new Scene(root, 300, 275));
            //stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}