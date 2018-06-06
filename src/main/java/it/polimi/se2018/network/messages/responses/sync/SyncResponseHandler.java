package it.polimi.se2018.network.messages.responses.sync;

import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;
import it.polimi.se2018.network.messages.responses.sync.modelupdates.*;

/**
 * This is the interface implemented by the clas handling the responses
 * @author Francesco Lorenzo
 */
public interface SyncResponseHandler {

    /**
     * This method updates the state of the game client-side
     * @param modelViewResponse is the updated version of the model
     */
    void handleResponse(ModelViewResponse modelViewResponse);

    /**
     * This method is used to inform the Client of textual messages from the Server, such as Exceptions
     * @param textResponse is the response containing the message
     */
    void handleResponse(TextResponse textResponse);

    /**
     * This method is used to notify the client that he can use the Tool Card he selected
     * @param toolCardResponse contains the value of the Tool Card to be used
     */
    void handleResponse(ToolCardResponse toolCardResponse);

    /**
     * This method is used by the Server during the setup of the game
     * @param setupResponse contains 4 windows to choose from, the Tool Cards extracted and the Objectives
     */
    void handleResponse(SetupResponse setupResponse);

    /**
     * This method is used by the Server to communicate a specific color for {@link ToolCard}
     * @param inputResponse contains the color chosen for the {@link Die}
     */
    void handleResponse(InputResponse inputResponse);

    /**
     * This method is used by the Server to send the final scores
     * @param scoreBoardResponse contains the list of the players in descending order
     */
    void handleResponse(ScoreBoardResponse scoreBoardResponse);

    /**
     * This method is used by the Server to communicate objects to a player that has reconnected
     * @param reconnectionResponse contains all useful objects for reconnection
     */
    void handleResponse(ReconnectionResponse reconnectionResponse);

    void handleResponse(DraftPoolResponse draftPoolResponse);

    void handleResponse(RoundTrackerResponse roundTrackerResponse);

    void handleResponse(WindowResponse windowResponse);

    void handleResponse(ModelUpdateResponse modelUpdateResponse);
}
