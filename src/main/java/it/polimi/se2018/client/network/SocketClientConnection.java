package it.polimi.se2018.client.network;

import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.client.view.cli.CLIController;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.utils.WaitingThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketClientConnection extends ClientConnection implements Runnable {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private boolean isOpen;

    public SocketClientConnection(ClientView clientView, Socket socket, ObjectInputStream in, ObjectOutputStream out){
        super(clientView);
        this.socket = socket;
        this.in = in;
        this.out = out;
        isOpen = true;
    }

    private void closeConnection(){
        try{
            in.close();
            out.close();
            socket.close();
        }catch(IOException e){
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
    }

    @Override
    public void sendMessage(Message message){
        try{
            out.writeObject(message);
        }catch(IOException e){
            //riconnettiti
        }
    }

    @Override
    public void stop(){
        isOpen = false;
    }

    @Override
    public void run(){
        while(isOpen){
            try{
                Response response = (Response) in.readObject();
                if (response != null) response.handleClass(this);
            }
            catch(ClassNotFoundException | IOException e) {
                //riconnettiti
            }
        }
        closeConnection();
    }
}
