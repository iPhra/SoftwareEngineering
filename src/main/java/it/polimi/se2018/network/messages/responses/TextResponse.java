package it.polimi.se2018.network.messages.responses;

import it.polimi.se2018.model.Player;

public class TextResponse extends Response {
    private final String message;

    public TextResponse(Player player, String message){
        super(player);
        this.message=message;
    }

    public void handle(ResponseHandler responseHandler) {responseHandler.handleResponse(this);}

    public String getMessage() {return message;}
}
