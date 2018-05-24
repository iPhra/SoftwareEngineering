package it.polimi.se2018.network.messages.requests;

/**
 * This class represents a message from the Client to the Server to communicate the value chosen for a die
 * @author Francesco Lorenzo
 */
public class InputMessage extends Message {
    /**
     * It's the chosen value for the die
     */
    private final int dieValue;

    public InputMessage(int playerID, int stateID, int dieValue) {
        super(playerID,stateID);
        this.dieValue = dieValue;
    }

    /**
     * @return the value of the die
     */
    public int getDieValue() {
        return dieValue;
    }

    /**
     * Uses the handler to handle this specific input request
     * @param handler is the object who will handle this request
     */
    @Override
    public void handle(MessageHandler handler) {
        handler.handleMove(this);
    }
}
