package it.polimi.se2018.network.messages.responses.sync;

/**
 * This is the class containing a textual response from the Server to the Client
 */
public class TextResponse extends SyncResponse {

    public TextResponse(int playerID){
        super(playerID);
    }

    /**
     * Uses the handler to handle this specific textual response
     * @param syncResponseHandler is the object who will handle this response
     */
    @Override
    public void handle(SyncResponseHandler syncResponseHandler) {
        syncResponseHandler.handleResponse(this);}


}
