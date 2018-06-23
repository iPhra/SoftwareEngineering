package it.polimi.se2018.network.messages.requests.toolcardmessage;

import it.polimi.se2018.network.messages.requests.MessageHandler;

public class LathekinMessage extends ToolCardMessageAbstract {
    LathekinMessage(int playerID, int stateID, int toolCardNumber) {
        super(playerID, stateID, toolCardNumber);
    }

    @Override
    public void handle(MessageHandler handler) {

    }
}
