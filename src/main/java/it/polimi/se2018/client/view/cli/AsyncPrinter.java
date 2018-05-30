package it.polimi.se2018.client.view.cli;

import it.polimi.se2018.network.messages.responses.DisconnectionResponse;

public class AsyncPrinter implements Runnable {
    private final CLIInput cliInput;
    private final CLIClientView cliClientView;
    private final DisconnectionResponse disconnectionResponse;

    AsyncPrinter(CLIInput cliInput, CLIClientView cliClientView, DisconnectionResponse disconnectionResponse) {
        this.cliInput = cliInput;
        this.disconnectionResponse = disconnectionResponse;
        this.cliClientView = cliClientView;
    }

    @Override
    public void run() {
        cliInput.print("");
        cliInput.print("Player " + disconnectionResponse.getPlayerName() + " has disconnected!");
        if(disconnectionResponse.getMessage()!=null) {
            cliInput.print(disconnectionResponse.getMessage() + "\n");
            cliClientView.halt("Game is over");
        }
    }
}
