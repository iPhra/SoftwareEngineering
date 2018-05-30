package it.polimi.se2018.client.view.cli;


public class AsyncPrinter implements Runnable {
    private final CLIView cliView;
    private final CLIController cliController;
    private final String message;
    private final boolean halt;

    public AsyncPrinter(CLIView cliView, CLIController cliController, String message, boolean halt) {
        this.cliView = cliView;
        this.cliController = cliController;
        this.message = message;
        this.halt = halt;
    }

    @Override
    public void run() {
        cliView.print(message);
        if(halt) {
            cliController.halt("Stopped");
        }
    }
}