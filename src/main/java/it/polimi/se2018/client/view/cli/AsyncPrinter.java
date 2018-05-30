package it.polimi.se2018.client.view.cli;

import it.polimi.se2018.network.messages.responses.DisconnectionResponse;

public class AsyncPrinter implements Runnable {
    private final CLIView cliView;
    private final CLIController cliController;
    private final DisconnectionResponse disconnectionResponse;

    AsyncPrinter(CLIView cliView, CLIController cliController, DisconnectionResponse disconnectionResponse) {
        this.cliView = cliView;
        this.disconnectionResponse = disconnectionResponse;
        this.cliController = cliController;
    }

    @Override
    public void run() {
        cliView.print("");
        cliView.print("Player " + disconnectionResponse.getPlayerName() + " has disconnected!");
        if(disconnectionResponse.getMessage()!=null) {
            cliView.print(disconnectionResponse.getMessage() + "\n");
            cliController.halt("Game is over");
        }
    }
}