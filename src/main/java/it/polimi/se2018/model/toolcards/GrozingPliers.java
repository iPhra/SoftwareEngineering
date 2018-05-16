package it.polimi.se2018.model.toolcards;

import it.polimi.se2018.controller.ToolCardHandler;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.utils.exceptions.ToolCardException;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.view.CLI.CLIClientView;

public class GrozingPliers extends ToolCard {

    public GrozingPliers(String imagePath) {
        super(imagePath, "Grozing Pliers", "After drafting, increase or decrease the value of the drafted die by 1");
    }

    @Override
    public void handle(ToolCardHandler handler, ToolCardMessage message) throws ToolCardException{
        handler.useCard(this, message);
    }
    public ToolCardMessage getPlayerRequests(int playerID, CLIClientView clientView, int toolcardnumber) {
        int choice = clientView.getMinusPlus();
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, toolcardnumber);
        toolCardMessage.addValue(choice);
        return  toolCardMessage;
    }
}
