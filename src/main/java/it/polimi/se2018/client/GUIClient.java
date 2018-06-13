package it.polimi.se2018.client;

import it.polimi.se2018.client.network.RMIClientConnection;
import it.polimi.se2018.client.network.SocketClientConnection;
import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.client.view.gui.GUIView;
import it.polimi.se2018.network.connections.rmi.RemoteConnection;
import it.polimi.se2018.network.connections.rmi.RemoteManager;

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

public class GUIClient extends Client implements Runnable{
    private GUIView clientView;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private RemoteManager manager;

    public GUIClient() {
        super();
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
            }
            catch(IOException | ClassNotFoundException  e){
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
            }
            catch(RemoteException | NotBoundException | MalformedURLException e){
                System.exit(1);
            }
        }
        return setup;
    }

    public boolean getPlayerName(String playerName){
        if(isSocket) return getPlayerNameSocket(playerName);
        else return getPlayerNameRMI(playerName);
    }

    public ClientView getGUIView() {
        return clientView;
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
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

    @Override
    void startNewGame() {
        setup = false;
        //implementa
        //alla fine devi tornare in wait tramite waitForAction()
    }

    @Override
    void handleDisconnection() {
        //get del guiController che usa il sceneController per cambiare scena
        //alla fine devi tornare in wait tramite waitForAction()
    }

    @Override
    public void run() {
        waitForAction();
    }
}