package it.polimi.se2018.network.messages.requests;


import it.polimi.se2018.mvc.model.toolcards.ToolCard;

/**
 * This is the interface implemented by the class who will perform the move associated to a message
 */
public interface MessageHandler {

    /**
     * This method uses a Tool Card
     * @param toolCardMessage contains the parameters needed by the {@link ToolCard}
     */
    void handleMove(ToolCardMessage toolCardMessage);

    /**
     * This method ends the player's turn
     * @param passMessage contains the player whishing to pass
     */
    void handleMove(PassMessage passMessage);

    /**
     * This method places the drafted die
     * @param placeMessage contains the position where the player wants to place his die
     */
    void handleMove(PlaceMessage placeMessage);

    /**
     * This method drafts a die
     * @param draftMessage contains the position in the Draft Pool of the die the player wants to draft
     */
    void handleMove(DraftMessage draftMessage);

    /**
     * This method checks if you can use a specific Tool Card
     * @param toolCardRequestMessage contains the value of the tool card you want to use
     */
    void handleMove(ToolCardRequestMessage toolCardRequestMessage);

    void handleMove(SetupMessage setupMessage);

    void handleMove(InputMessage inputMessage);
}
