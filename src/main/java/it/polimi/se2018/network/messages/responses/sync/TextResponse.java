package it.polimi.se2018.network.messages.responses.sync;

/**
 * This is the class containing a textual response from the Server to the Client
 */
public class TextResponse extends SyncResponse {
    private final String message;

    public TextResponse(int player, String message){
        super(player);
        this.message=message;
    }

    /**
     * @return the String containing the information the Server wants the Client to know
     */
    public String getMessage() {return message;}

    /**
     * Uses the handler to handle this specific textual response
     * @param syncResponseHandler is the object who will handle this response
     */
    @Override
    public void handle(SyncResponseHandler syncResponseHandler) {
        syncResponseHandler.handleResponse(this);}


}
