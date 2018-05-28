package it.polimi.se2018.network.messages.responses;

/**
 * This is the class containing a response from the Server containing a notification that a player has disconnected
 * @author Francesco Lorenzo
 */
public class DisconnectionResponse extends Response{
    private final String message;
    private final String playerName;

    public DisconnectionResponse(int playerID, String message, String playerName) {
        super(playerID);
        this.message = message;
        this.playerName = playerName;
    }

    public String getMessage() {
        return message;
    }

    public String getPlayerName() {
        return playerName;
    }

    /**
     * Uses the handler to handle this specific disconnection response
     *
     * @param responseHandler is the object who will handle this response
     */
    @Override
    public void handle(ResponseHandler responseHandler) {
        responseHandler.handleResponse(this);
    }
}
