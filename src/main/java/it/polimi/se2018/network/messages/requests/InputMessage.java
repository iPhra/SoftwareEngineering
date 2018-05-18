package it.polimi.se2018.network.messages.requests;

public class InputMessage extends Message {
    int dieValue;

    public InputMessage(int playerID, int dieValue) {
        super(playerID);
        this.dieValue = dieValue;
    }

    public int getDieValue() {
        return dieValue;
    }

    @Override
    public void handle(MessageHandler handler) {
        handler.performMove(this);
    }
}
