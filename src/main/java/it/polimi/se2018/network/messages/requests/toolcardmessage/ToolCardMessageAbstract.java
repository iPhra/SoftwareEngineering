package it.polimi.se2018.network.messages.requests.toolcardmessage;

import it.polimi.se2018.network.messages.requests.Message;

public abstract class ToolCardMessageAbstract extends Message {
    final int toolCardNumber;

    ToolCardMessageAbstract(int playerID, int stateID, int toolCardNumber) {
        super(playerID, stateID);
        this.toolCardNumber = toolCardNumber;
    }
}
