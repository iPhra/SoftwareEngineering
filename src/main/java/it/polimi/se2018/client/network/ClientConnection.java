package it.polimi.se2018.client.network;

import it.polimi.se2018.client.Client;
import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.client.view.cli.StopperThread;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.*;
import it.polimi.se2018.network.messages.responses.sync.SyncResponse;

/**
 * This is the abstract class containing methods used by both RMI and Socket
 */
public abstract class ClientConnection implements ResponseHandler {
    private static final String LAST_PLAYER = "You are the last player remaining, you win!";

    /**
     * This is the view of the client, who will be notified when a message is received
     */
    private final ClientView clientView;

    /**
     * This is the client of the game
     */
    Client client;

    /**
     * {@code true} if the match is still playing
     */
    boolean matchPlaying;

    /**
     * {@code true} if the connection doesn't have to be stopped
     */
    boolean isOpen;

    ClientConnection(ClientView clientView) {
        this.clientView = clientView;
    }

    /**
     * This method sends a message to the Server through the network
     * @param message is the message to send
     */
    public abstract void sendMessage(Message message);

    /**
     * This method stops the thread when a match finishes or the player disconnects
     */
    public abstract void stop();

    /**
     * This method is called when the network realizes that the connection has dropped
     */
    void disconnect() {
        if(matchPlaying) {
            if(clientView.isIterating()) { //if the client is currently getting an input
                Thread thread = new Thread(new StopperThread(clientView,"",true));
                thread.start();
            }
            client.setDisconnected();
        }
    }

    @Override
    public void handleResponse(SyncResponse syncResponse) {
        clientView.handleNetworkInput(syncResponse);
    }

    @Override
    public void handleResponse(DisconnectionResponse disconnectionResponse) {
        String message;
        message = "\nPlayer " + disconnectionResponse.getPlayerName() + " has disconnected!\n";
        new Thread(new StopperThread(clientView,message,false)).start();
    }

    @Override
    public void handleResponse(ReconnectionNotificationResponse reconnectionNotificationResponse) {
        String message;
        message = "\nPlayer " + reconnectionNotificationResponse.getPlayerName() + " has reconnected!\n";
        new Thread(new StopperThread(clientView,message,false)).start();
    }

    @Override
    public void handleResponse(TimeUpResponse timeUpResponse) {
        new Thread(new StopperThread(clientView,"Time is up",true)).start();
    }

    @Override
    public void handleResponse(EndGameResponse endGameResponse) {
        stop();
        if (endGameResponse.getScoreBoardResponse().isLastPlayer()) {
            Thread thread;
            if (endGameResponse.isPlayerPlaying()) {
                thread = new Thread(new StopperThread(clientView, LAST_PLAYER,true));
            }
            else {
                thread = new Thread(new StopperThread(clientView, LAST_PLAYER,false));
            }
            thread.start();
            try {
                thread.join();
            }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        clientView.handleNetworkInput(endGameResponse.getScoreBoardResponse());
    }
}
