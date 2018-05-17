package it.polimi.se2018.network.connections;

import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.view.ServerView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketServerConnection implements Runnable, ServerConnection{
    private Server server;
    private Socket socket;
    private String playerName;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean isOpen;
    private ServerView serverView;

    SocketServerConnection(Socket socket, ServerView serverView, Server server){
        this.socket = socket;
        this.serverView = serverView;
        this.server = server;
        isOpen = true;
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            this.in = new ObjectInputStream(socket.getInputStream());
        }catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void sendResponse(Response response){
        try{
            out.writeObject(response);
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }

    private void setup() throws IOException, ClassNotFoundException{
        boolean setup = true;
        int playerID = server.generateID();
        out.writeObject(playerID);
        while (setup){
            playerName = (String) in.readObject();
            setup = !server.checkName(playerID, playerName);
            out.writeObject(setup);
        }
        server.setPlayer(playerID,playerName,this);
        serverView.setServerConnections(playerID, this);
    }

    @Override
    public void run(){
        try{
            setup();
            while(isOpen){
                Message message = (Message) in.readObject();
                if (message != null) serverView.handleNetworkInput(message);
            }
        }catch(ClassNotFoundException | IOException e){
            System.err.println(e.getMessage());
        }
        closeConnection();
    }

    public void stop(){
        isOpen = false;
    }

    private void closeConnection(){
        stop();
        try{
            in.close();
            out.close();
            socket.close();
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }


}
