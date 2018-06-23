package it.polimi.se2018.network.messages.requests.toolcardmessage;

import it.polimi.se2018.network.messages.requests.MessageHandler;

public class RunningPliersMessage extends ToolCardMessageAbstract {


    RunningPliersMessage(int playerID, int stateID, int toolCardNumber) {
        super(playerID, stateID, toolCardNumber);
    }

    @Override
    public void handle(MessageHandler handler) {

    }
}
