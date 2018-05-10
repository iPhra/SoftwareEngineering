package it.polimi.se2018.Network.Messages.Responses;

import it.polimi.se2018.Model.Player;

public class TurnStartResponse extends Response {

    public TurnStartResponse(Player player) {
        super(player);
    }

    public void handle(ResponseHandler responseHandler) {
        responseHandler.handleResponse(this);
    }


}
