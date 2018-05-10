package it.polimi.se2018.network.messages.requests;

import it.polimi.se2018.model.Player;

public class ToolCardRequestMessage extends Message {
    private final int toolCardNumber;

    public ToolCardRequestMessage(Player player, int toolCardNumber) {
        super(player);
        this.toolCardNumber=toolCardNumber;

    }


    public int getToolCardNumber() {
        return toolCardNumber;
    }

    @Override
    public void handle(MessageHandler handler) {
        handler.performMove(this);
    }
}
