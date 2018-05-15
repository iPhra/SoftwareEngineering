package it.polimi.se2018.network.connections;

import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.view.ClientView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClientConnection implements Runnable, ClientConnection{
    private final Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ClientView clientView;
    private boolean isOpen;

    SocketClientConnection(Socket socket, ClientView clientView){
        this.socket = socket;
        try{
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream((socket.getOutputStream()));
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
        this.clientView = clientView;
    }

    public void run(){
        isOpen = true;
        while(isOpen){
            try{
                Response response = (Response) in.readObject();
                if (response != null) clientView.handleNetworkInput(response);
            }catch(ClassNotFoundException | IOException e){
                System.err.println(e.getMessage());
            }
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

    @Override
    public void sendMessage(Message message){
        try{
            out.writeObject(message);
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }

}
