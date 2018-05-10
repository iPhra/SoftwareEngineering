package it.polimi.se2018.network.messages.responses;


import it.polimi.se2018.model.Player;

import java.util.List;

public class ToolCardResponse extends Response{

    private List<String> playerRequests;

    public ToolCardResponse(Player player, List<String> playerRequests) {
        super(player);
        this.playerRequests=playerRequests;
    }

    public void handle(ResponseHandler responseHandler) {responseHandler.handleResponse(this);}

    public List<String> getPlayerRequests() {
        return playerRequests;
    }
}
