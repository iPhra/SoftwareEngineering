package it.polimi.se2018.network.messages.responses.sync;


import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.network.messages.responses.ResponseHandler;

public abstract class SyncResponse extends Response {
    private String description;

    protected SyncResponse(int playerID) {
        super(playerID);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Uses the handler to handle this specific response
     * Implemented by each specific SyncResponse subclass
     * @param syncResponseHandler is the object who will handle this response
     */
    public abstract void handle(SyncResponseHandler syncResponseHandler);

    @Override
    public void handleClass(ResponseHandler responseHandler) {
        responseHandler.handleResponse(this);
    }
}
