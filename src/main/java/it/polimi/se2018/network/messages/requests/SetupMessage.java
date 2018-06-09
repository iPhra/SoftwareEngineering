package it.polimi.se2018.network.messages.requests;

import it.polimi.se2018.mvc.model.Window;

/**
 * This class represents a message from the Client to the Server with the window chosen by said player
 */
public class SetupMessage extends Message{
    /**
     * This is the window chosen by the player
     */
    private final Window window;

    public SetupMessage(int playerID, int stateID, Window window) {
        super(playerID,stateID);
        this.window = window;
    }

    /**
     * @return the window chosen by the player
     */
    public Window getWindow() {
        return window;
    }

    /**
     * Uses the handler to handle this specific setup request
     * @param messageHandler is the object who will handle this message
     */
    public void handle(MessageHandler messageHandler){
        messageHandler.handleMove(this);
    }
}
