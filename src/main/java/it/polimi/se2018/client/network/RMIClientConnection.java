package it.polimi.se2018.client.network;

import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.client.view.cli.AsyncPrinter;
import it.polimi.se2018.client.view.cli.CLIController;
import it.polimi.se2018.client.view.cli.CLIView;
import it.polimi.se2018.network.connections.rmi.RemoteConnection;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.DisconnectionResponse;
import it.polimi.se2018.network.messages.responses.ReconnectionNotificationResponse;
import it.polimi.se2018.network.messages.responses.Response;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class RMIClientConnection implements ClientConnection, RemoteConnection, Runnable {
    private final ClientView clientView;
    private final List<Response> events;
    private RemoteConnection serverConnection;
    private CLIController cliController;
    private boolean isOpen;

    public RMIClientConnection(ClientView clientView) {
        this.clientView = clientView;
        events = new ArrayList<>();
        isOpen = true;
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

    public void setServerConnection(RemoteConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    @Override
    public void getResponse(Response response) {
        synchronized (events) {
            events.add(response);
            events.notifyAll();
        }
    }

    @Override
    public void getMessage(Message message) { //not implemented client-side
    }

    @Override
    public void updateView() {
        synchronized (events) {
            while (events.isEmpty()) {
                try {
                    events.wait();
                } catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            }
        }
        Response response = events.remove(0);
        if(response instanceof DisconnectionResponse) printDisconnection((DisconnectionResponse) response);
        else if(response instanceof ReconnectionNotificationResponse) printReconnection((ReconnectionNotificationResponse) response);
        else clientView.handleNetworkInput(response);
    }

    @Override
    public void sendMessage(Message message){
        try {
            serverConnection.getMessage(message);
        }
        catch(RemoteException e) {
            //riconnettiti
        }
    }

    @Override
    public void stop() {
        isOpen = false;
    }

    @Override
    public void setCLIController(CLIController cliController) {
        this.cliController=cliController;
    }

    @Override
    public void run() {
        while(isOpen) {
            updateView();
        }
    }
}
