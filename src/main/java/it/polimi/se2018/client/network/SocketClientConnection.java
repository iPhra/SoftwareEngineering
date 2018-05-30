package it.polimi.se2018.client.network;

import it.polimi.se2018.client.view.cli.AsyncPrinter;
import it.polimi.se2018.client.view.cli.CLIController;
import it.polimi.se2018.client.view.cli.CLIView;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.DisconnectionResponse;
import it.polimi.se2018.network.messages.responses.ReconnectionNotificationResponse;
import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.client.view.ClientView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketClientConnection implements ClientConnection, Runnable {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final ClientView clientView;
    private CLIController cliController;
    private boolean isOpen;

    public SocketClientConnection(Socket socket, ClientView clientView, ObjectInputStream in, ObjectOutputStream out){
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.clientView = clientView;
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

    private void printDisconnection(DisconnectionResponse disconnectionResponse) {
        String message;
        message = "\nPlayer " + disconnectionResponse.getPlayerName() + " has disconnected!\n";
        if(disconnectionResponse.getMessage()!= null) {
            new Thread(new AsyncPrinter((CLIView)clientView,cliController,message+disconnectionResponse.getMessage() + "\n",true)).start();
        }
        else new Thread(new AsyncPrinter((CLIView)clientView,cliController,message,false)).start();
    }

    private void printReconnection(ReconnectionNotificationResponse reconnectionNotificationResponse) {
        String message;
        message = "\nPlayer " + reconnectionNotificationResponse.getPlayerName() + " has reconnected!\n";
        new Thread(new AsyncPrinter((CLIView)clientView,cliController,message,false)).start();
    }

    @Override
    public void setCLIController(CLIController cliController) {
        this.cliController=cliController;
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
                if (response != null) {
                    if(response instanceof DisconnectionResponse) printDisconnection((DisconnectionResponse) response);
                    else if(response instanceof ReconnectionNotificationResponse) printReconnection((ReconnectionNotificationResponse) response);
                    else clientView.handleNetworkInput(response);
                }
            }
            catch(ClassNotFoundException | IOException e) {
                //riconnettiti
            }
        }
        closeConnection();
    }
}
