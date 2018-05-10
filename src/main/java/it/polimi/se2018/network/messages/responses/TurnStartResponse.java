package it.polimi.se2018.network.messages.responses;

import it.polimi.se2018.model.Player;

public class TurnStartResponse extends Response {

    public TurnStartResponse(Player player) {
        super(player);
    }

    public void handle(ResponseHandler responseHandler) {
        responseHandler.handleResponse(this);
    }


}
