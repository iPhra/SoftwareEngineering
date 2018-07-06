package it.polimi.se2018.network.messages.requests;

/**
 * This class represents a message from a player wishing to draft a die
 */
public class DraftMessage extends Message {

    /**
     * This is the position in the Draft Pool of the die the player wants to draft
     */
    private int draftPoolPosition;

    public DraftMessage(int playerID, int stateID, int draftPoolPosition) {
        super(playerID,stateID);
        this.draftPoolPosition = draftPoolPosition;
    }

    /**
     *
     * @return the position of the die the player wants to draft
     */
    public int getDraftPoolPosition() {
        return draftPoolPosition;
    }

    /**
     * Sets the position of the die
     * @param draftPoolPosition is the position in the Draft Pool where the player wants to draft a die from
     */
    public void setDraftPoolPosition(int draftPoolPosition) {
        this.draftPoolPosition = draftPoolPosition;
    }

    /**
     * Uses the handler to handle this specific draft place request
     * @param handler is the object who will handle this request
     */
    @Override
    public void handle(MessageHandler handler) {
        handler.handleMove(this);
    }
}
