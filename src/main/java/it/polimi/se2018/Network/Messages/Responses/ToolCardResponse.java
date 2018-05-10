package it.polimi.se2018.Network.Messages.Responses;


import it.polimi.se2018.Model.Player;

import java.util.List;

public class ToolCardResponse extends Response{

    private List<String> playerRequests;

    public ToolCardResponse(Player player, List<String> playerRequests) {
        super(player);
        this.playerRequests=playerRequests;
    }

    public void handle(ResponseHandler responseHandler) {responseHandler.handleResponse(this);}
}
