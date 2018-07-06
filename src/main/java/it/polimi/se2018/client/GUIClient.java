package it.polimi.se2018.client;

import it.polimi.se2018.client.network.RMIClientConnection;
import it.polimi.se2018.client.network.SocketClientConnection;
import it.polimi.se2018.client.view.gui.GUIView;
import it.polimi.se2018.client.view.gui.controllers.MatchHandler;
import it.polimi.se2018.client.view.gui.controllers.SceneController;
import it.polimi.se2018.network.connections.rmi.RemoteConnection;
import it.polimi.se2018.network.connections.rmi.RemoteManager;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GUIClient extends Client {
    private GUIView guiView;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private RemoteManager manager;

    GUIClient() {
        super();
    }

    private boolean setPlayerNameSocket(String playerName){
        if (setup) {
            this.nickname = playerName;
            try{
                out.writeObject(playerName);
                setup = (boolean) in.readObject();
                if (!setup) {
                    playerID = (int) in.readObject();
                    guiView = new GUIView(this,playerID);
                    clientConnection = new SocketClientConnection(this, guiView, socket,in,out);
                    guiView.setClientConnection(clientConnection);
                    new Thread((SocketClientConnection) clientConnection).start();
                    new Thread(guiView).start();
                }
            }
            catch(IOException | ClassNotFoundException  e){
                System.exit(1);
            }
        }
        return setup;
    }

    private boolean setPlayerNameRMI(String playerName){
        if (setup){
            this.nickname = playerName;
            try{
                setup = manager.checkName(playerName);
                if(!setup) {
                    playerID = manager.getID(playerName);
                    guiView = new GUIView(this,playerID);
                    clientConnection = new RMIClientConnection(this, guiView);
                    manager.addClient(playerID, playerName, (RemoteConnection) UnicastRemoteObject.exportObject((RemoteConnection) clientConnection, 0));
                    RemoteConnection serverConnection = (RemoteConnection) Naming.lookup("//localhost/ServerConnection" + playerID);
                    ((RMIClientConnection) clientConnection).setServerConnection(serverConnection);
                    guiView.setClientConnection(clientConnection);
                    new Thread((RMIClientConnection) clientConnection).start();
                    new Thread(guiView).start();
                }
            }
            catch(RemoteException | NotBoundException | MalformedURLException e){
                System.exit(1);
            }
        }
        return setup;
    }

    public boolean setPlayerName(String playerName){
        if(isSocket) return setPlayerNameSocket(playerName);
        else return setPlayerNameRMI(playerName);
    }

    public GUIView getGUIView() {
        return guiView;
    }

    public void setDifferentParams(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void createRMIConnection(){
        try {
            manager = (RemoteManager) Naming.lookup("//localhost/RemoteManager");
        }
        catch(RemoteException | NotBoundException | MalformedURLException e){
            System.exit(1);
        }
    }

    public void createSocketConnection(){
        try{
            socket = new Socket(ip, port);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        }
        catch(IOException e){
            System.exit(1);
        }
    }

    @Override
    public void setGameEnded() {
        setup = true;
    }

    @Override
    public void setDisconnected() {
        setup = true;
        clientConnection.stop();
        guiView.stop();
        Platform.runLater(() -> {
            SceneController sceneController = guiView.getGuiLogic().getSceneController();
            ((MatchHandler)sceneController).returnToSetConnectionScene(sceneController.getScene());
        });
    }
}