package it.polimi.se2018.network.messages.requests.toolcardmessage;

import it.polimi.se2018.network.messages.requests.MessageHandler;

public class GlazingHammerMessage extends ToolCardMessageAbstract {

    GlazingHammerMessage(int playerID, int stateID, int toolCardNumber) {
        super(playerID, stateID, toolCardNumber);
    }

    @Override
    public void handle(MessageHandler handler) {

    }
}
