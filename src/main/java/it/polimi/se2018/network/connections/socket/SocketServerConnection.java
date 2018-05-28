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
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketServerConnection extends ServerConnection implements Runnable{
    private final Server server;
    private final Socket socket;
    private String playerName;
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
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

    private void setup() {
        try {
            boolean setup = true;
            /*String email = (String) in.readObject();
            playerID = server.checkEmail(email);
            if (playerID != -1) {
                setup = false;
                out.writeObject(setup);
            }
            else {
                out.writeObject(setup);
                playerID = Server.generateID();
                server.writePlayerID(email, playerID);
            }*/
            playerID = Server.generateID();
            out.writeObject(playerID);
            while (setup) {
                playerName = (String) in.readObject();
                setup = server.checkName(playerID, playerName);
                out.writeObject(setup);
            }
        }
        catch(IOException | ClassNotFoundException e) {
            Server.decrementID();
            stop();
        }
        server.setPlayer(playerID, playerName, this);
    }

    private void closeConnection(){
        try{
            in.close();
            out.close();
            socket.close();
        }
        catch(IOException e){
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

    @Override
    public void sendResponse(Response response){
        try{
            if(!isDisconnected()) out.writeObject(response);
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
    }

    @Override
    public synchronized void run() {
        setup();
        while (isOpen) {
            try {
                if(isDisconnected()) this.wait();
                Message message = (Message) in.readObject();
                if (message != null) serverView.handleNetworkInput(message);
            }
            catch (ClassNotFoundException e) {
                Logger logger = Logger.getAnonymousLogger();
                logger.log(Level.ALL, e.getMessage());
            }
            catch (IOException e) {
                server.handleDisconnection(playerID);
            }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        closeConnection();
    }
}
