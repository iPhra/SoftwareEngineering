package it.polimi.se2018.model.toolcards;

import it.polimi.se2018.controller.ToolCardHandler;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.view.cli.CLIClientView;

public class GlazingHammer extends ToolCard {

    public GlazingHammer(String imagePath) {
        super(imagePath, "Glazing Hammer", "Re-roll all dice in the Draft Pool");
    }
    @Override
    public void handle(ToolCardHandler handler, ToolCardMessage message) {
        handler.useCard(this, message);
    }

    public ToolCardMessage getPlayerRequests(int playerID, CLIClientView clientView, int toolcardnumber) {
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, toolcardnumber);
        return  toolCardMessage;
    }
}
