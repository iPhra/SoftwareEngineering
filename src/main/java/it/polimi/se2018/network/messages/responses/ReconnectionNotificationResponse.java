package it.polimi.se2018.network.messages.responses;

public class ReconnectionNotificationResponse extends Response {
    private final String playerName;

    public ReconnectionNotificationResponse(int playerID, String playerName) {
        super(playerID);
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    /**
     * Uses the handler to handle this specific reconnection notification response
     *
     * @param responseHandler is the object who will handle this response
     */
    @Override
    public void handle(ResponseHandler responseHandler) {
        //not implemented
    }
}
