package it.polimi.se2018.client.network;

import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.client.view.cli.AsyncPrinter;
import it.polimi.se2018.client.view.cli.CLIController;
import it.polimi.se2018.client.view.cli.CLIView;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.DisconnectionResponse;
import it.polimi.se2018.network.messages.responses.ReconnectionNotificationResponse;
import it.polimi.se2018.network.messages.responses.ResponseHandler;
import it.polimi.se2018.network.messages.responses.TurnEndResponse;
import it.polimi.se2018.network.messages.responses.sync.SyncResponse;
import it.polimi.se2018.utils.WaitingThread;

import java.time.Duration;


public abstract class ClientConnection implements ResponseHandler {
    private final ClientView clientView;
    private CLIController cliController;

    public ClientConnection(ClientView clientView) {
        this.clientView = clientView;
    }

    public abstract void sendMessage(Message message);

    public abstract void stop();

    public void setCLIController(CLIController cliController) {
        this.cliController = cliController;
    }

    @Override
    public void handleResponse(SyncResponse syncResponse) {
        clientView.handleNetworkInput(syncResponse);
    }

    @Override
    public void handleResponse(DisconnectionResponse disconnectionResponse) {
        String message;
        message = "\nPlayer " + disconnectionResponse.getPlayerName() + " has disconnected!\n";
        if(disconnectionResponse.getMessage()!= null) {
            new Thread(new AsyncPrinter((CLIView)clientView,cliController,message+disconnectionResponse.getMessage() + "\n\n",true)).start();
        }
        else new Thread(new AsyncPrinter((CLIView)clientView,cliController,message,false)).start();
    }

    @Override
    public void handleResponse(ReconnectionNotificationResponse reconnectionNotificationResponse) {
        String message;
        message = "\nPlayer " + reconnectionNotificationResponse.getPlayerName() + " has reconnected!\n";
        new Thread(new AsyncPrinter((CLIView)clientView,cliController,message,false)).start();
    }

    @Override
    public void handleResponse(TurnEndResponse turnEndResponse) {
        new Thread(new AsyncPrinter((CLIView)clientView,cliController,"Your turn is over",true));
    }
}
