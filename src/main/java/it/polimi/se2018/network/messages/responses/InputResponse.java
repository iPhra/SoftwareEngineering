package it.polimi.se2018.network.messages.responses;

import it.polimi.se2018.model.Color;

public class InputResponse extends Response {
    private Color color;

    public InputResponse(int player, Color color) {
        super(player);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void handle(ResponseHandler responseHandler) { responseHandler.handleResponse(this);}
}
