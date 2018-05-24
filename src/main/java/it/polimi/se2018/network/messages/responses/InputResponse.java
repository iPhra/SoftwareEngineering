package it.polimi.se2018.network.messages.responses;

import it.polimi.se2018.mvc.model.Color;

/**
 * This class represents a response from the Server to the Client containing the color extracted for a die
 */
public class InputResponse extends Response {
    /**
     * This is the color extracted
     */
    private final Color color;

    public InputResponse(int player, Color color) {
        super(player);
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
     * @param responseHandler is the object who will handle this response
     */
    public void handle(ResponseHandler responseHandler) { responseHandler.handleResponse(this);}
}
