package it.polimi.se2018.network.messages.responses.sync;


import it.polimi.se2018.network.messages.responses.sync.modelupdates.ModelViewResponse;

public class ReconnectionResponse extends SyncResponse {
   private ModelViewResponse modelViewResponse;

    /**
     * This is the value of players in the current match
     */
    private int playersNumber;

    private final boolean windowsChosen;

    public ReconnectionResponse(int playerID, boolean windowsChosen) {
        super(playerID);
        this.windowsChosen = windowsChosen;
    }

    public void setModelViewResponse(ModelViewResponse modelViewResponse) {
        this.modelViewResponse = modelViewResponse;
    }

    public boolean isWindowsChosen() {
        return windowsChosen;
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    public ModelViewResponse getModelViewResponse() {
        return modelViewResponse;
    }

    /**
     * Uses the handler to handle this specific disconnection response
     *
     * @param syncResponseHandler is the object who will handle this response
     */
    @Override
    public void handle(SyncResponseHandler syncResponseHandler) {
        syncResponseHandler.handleResponse(this);
    }
}
