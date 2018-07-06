package it.polimi.se2018.network.connections.socket;

import it.polimi.se2018.network.Server;
import it.polimi.se2018.network.connections.ServerConnection;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.mvc.view.ServerView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketServerConnection implements ServerConnection, Runnable {
    private final Server server;
    private final Socket socket;
    private int playerID;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean isOpen;
    private ServerView serverView;

    public SocketServerConnection(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
        isOpen = true;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setup() {
        try {
            boolean setup = true;
            while (setup) {
                String nickname = (String) in.readObject();
                if(nickname.length()>15) out.writeObject(true);
                else {
                    if (server.checkRegistration(nickname)) { //if he's not registered
                        setup = false;
                        out.writeObject(false);
                        playerID = Server.generateID();
                        out.writeObject(playerID);
                        server.setPlayer(playerID, nickname, this);
                    } else if (server.checkDisconnection(nickname)) { //if he's reconnecting
                        setup = false;
                        out.writeObject(false);
                        playerID = server.getPlayerID(nickname);
                        out.writeObject(playerID);
                        server.handleReconnection(server.getPlayerID(nickname), this);
                    } else {
                        out.writeObject(true);
                    }
                }
            }
        }
        catch(IOException | ClassNotFoundException e) {
            stop();
        }
    }

    @Override
    public void sendResponse(Response response){
        try{
            out.writeObject(response);
        }
        catch(IOException e){
            server.handleDisconnection(playerID);
        }
    }

    @Override
    public void setServerView(ServerView serverView) {
        this.serverView = serverView;
    }

    @Override
    public void stop(){
        isOpen = false;
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void run() {
        setup();
        while (isOpen) {
            try {
                Message message = (Message) in.readObject();
                if (message != null) {
                    serverView.handleNetworkInput(message);
                }
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                server.handleDisconnection(playerID);
            }
        }
    }
}
