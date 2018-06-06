package it.polimi.se2018.client.view.cli;


import it.polimi.se2018.client.view.ClientView;

public class TimeStopper implements Runnable {
    private final ClientView clientView;
    private final String message;
    private final boolean halt;

    public TimeStopper(ClientView clientView, String message, boolean halt) {
        this.clientView = clientView;
        this.message = message;
        this.halt = halt;
    }

    @Override
    public void run() {
        clientView.handleAsyncEvent(halt,message);
    }
}