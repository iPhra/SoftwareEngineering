package it.polimi.se2018.network.messages.responses;

/**
 * This is the interface implemented by the clas handling the responses
 * @author Francesco Lorenzo
 */
public interface ResponseHandler {

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
     * This method notifies a client that his turn just started
     * @param turnStartResponse is an empty response
     */
    void handleResponse(TurnStartResponse turnStartResponse);

    /**
     * This method is used to notify the client that he can use the Tool Card he selected
     * @param toolCardResponse contains the number of the Tool Card to be used
     */
    void handleResponse(ToolCardResponse toolCardResponse);

    /**
     * This method is used by the Server during the setup of the game
     * @param setupResponse contains 4 windows to choose from, the Tool Cards extracted and the Objectives
     */
    void handleResponse(SetupResponse setupResponse);

    /**
     * This method is used by the Server to communicate a specific color for {@link it.polimi.se2018.model.toolcards.ToolCard}
     * @param inputResponse contains the color chosen for the {@link it.polimi.se2018.model.Die}
     */
    void handleResponse(InputResponse inputResponse);
}
