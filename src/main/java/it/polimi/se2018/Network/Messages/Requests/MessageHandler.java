package it.polimi.se2018.Network.Messages.Requests;

/**
 * This is the interface implemented by the class who will perform the move associated to a message
 * @Author Francesco Lorenzo
 */
public interface MessageHandler {

    /**
     * This method uses a Tool Card
     * @param toolCardMessage contains the parameters needed by the {@link it.polimi.se2018.Model.ToolCards.ToolCard}
     */
    public void performMove(ToolCardMessage toolCardMessage);

    /**
     * This method ends the player's turn
     * @param passMessage contains the player whishing to pass
     */
    public void performMove(PassMessage passMessage);

    /**
     * This method places the drafted die
     * @param placeMessage contains the position where the player wants to place his die
     */
    public void performMove(PlaceMessage placeMessage);

    /**
     * This method drafts a die
     * @param draftMessage contains the position in the Draft Pool of the die the player wants to draft
     */
    public void performMove(DraftMessage draftMessage);

    /**
     * This method checks if you can use a specific Tool Card
     * @param toolCardRequestMessage contains the number of the tool card you want to use
     */
    public void performMove(ToolCardRequestMessage toolCardRequestMessage);

}
