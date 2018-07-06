package it.polimi.se2018.network.messages.responses.sync;

import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.network.messages.responses.sync.SyncResponse;
import it.polimi.se2018.network.messages.responses.sync.SyncResponseHandler;

/**
 * This class represents a response from the Server to the Client containing the color extracted for a die
 */
public class InputResponse extends SyncResponse {
    /**
     * This is the color extracted
     */
    private final Color color;

    public InputResponse(int playerID, Color color) {
        super(playerID);
        this.color = color;
    }

    /**
     * @return the color of the die
     */
    public Color getColor() {
        return color;
    }

    /**
     * Uses the handler to handle this specific input response
     * @param syncResponseHandler is the object who will handle this response
     */
    public void handle(SyncResponseHandler syncResponseHandler) { syncResponseHandler.handleResponse(this);}
}
