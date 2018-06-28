package it.polimi.se2018.client;

import it.polimi.se2018.client.network.RMIClientConnection;
import it.polimi.se2018.client.network.SocketClientConnection;
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

public class GUIClient extends Client {
    private GUIView clientView;
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
                    clientView = new GUIView(this,playerID);
                    clientConnection = new SocketClientConnection(this, clientView, socket,in,out);
                    clientView.setClientConnection(clientConnection);
                    new Thread((SocketClientConnection) clientConnection).start();
                    new Thread(clientView).start();
                }
            }
            catch(IOException | ClassNotFoundException  e){
                e.printStackTrace();
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

    public boolean setPlayerName(String playerName){
        if(isSocket) return setPlayerNameSocket(playerName);
        else return setPlayerNameRMI(playerName);
    }

    public GUIView getGUIView() {
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
            e.printStackTrace();
        }
    }

    @Override
    public void setGameEnded() {
        setup = true;
    }

    @Override
    public void setDisconnected() {
        setup = true;
        //disconnetti?
    }
}