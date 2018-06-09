package it.polimi.se2018.client.network;

import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.client.view.cli.TimeStopper;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.*;
import it.polimi.se2018.network.messages.responses.sync.SyncResponse;


public abstract class ClientConnection implements ResponseHandler {
    private final ClientView clientView;

    ClientConnection(ClientView clientView) {
        this.clientView = clientView;
    }

    public abstract void sendMessage(Message message);

    public abstract void stop();

    @Override
    public void handleResponse(SyncResponse syncResponse) {
        clientView.handleNetworkInput(syncResponse);
    }

    @Override
    public void handleResponse(DisconnectionResponse disconnectionResponse) {
        String message;
        message = "\nPlayer " + disconnectionResponse.getPlayerName() + " has disconnected!\n";
        new Thread(new TimeStopper(clientView,message,false)).start();
    }

    @Override
    public void handleResponse(ReconnectionNotificationResponse reconnectionNotificationResponse) {
        String message;
        message = "\nPlayer " + reconnectionNotificationResponse.getPlayerName() + " has reconnected!\n";
        new Thread(new TimeStopper(clientView,message,false)).start();
    }

    @Override
    public void handleResponse(TimeUpResponse timeUpResponse) {
        new Thread(new TimeStopper(clientView,"Time is up",true)).start();
    }

    @Override
    public void handleResponse(EndGameResponse endGameResponse) {
        stop();
        if (endGameResponse.getScoreBoardResponse().isLastPlayer()) {
            Thread thread;
            if (endGameResponse.isPlayerPlaying()) {
                thread = new Thread(new TimeStopper(clientView, "You are the last player remaining, you win!",true));
            }
            else {
                thread = new Thread(new TimeStopper(clientView, "You are the last player remaining, you win!",false));
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
