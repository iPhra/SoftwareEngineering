package it.polimi.se2018.network.messages.requests;

import it.polimi.se2018.model.Window;

public class SetupMessage extends Message{
    private final Window window;

    public SetupMessage(int playerID, Window window) {
        super(playerID);
        this.window = window;
    }

    public Window getWindow() {
        return window;
    }

    public void handle(MessageHandler messageHandler){
        messageHandler.performMove(this);
    }
}
