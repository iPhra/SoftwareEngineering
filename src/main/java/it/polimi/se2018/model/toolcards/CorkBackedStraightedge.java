package it.polimi.se2018.model.toolcards;

import it.polimi.se2018.controller.ToolCardHandler;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.utils.exceptions.ToolCardException;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.view.CLI.CLIClientView;

public class CorkBackedStraightedge extends ToolCard {

    public CorkBackedStraightedge(String imagePath) {
        super(imagePath, "Cork Backed Straightedge", "After drafting, place the die in a spot that is not adjacent to another die");
    }

    @Override
    public void handle(ToolCardHandler handler, ToolCardMessage message) throws ToolCardException{
        handler.useCard(this, message);
    }
    public ToolCardMessage getPlayerRequests(int playerID, CLIClientView clientView, int toolcardnumber) {
        Coordinate finalPosition = clientView.getCoordinate();
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, toolcardnumber);
        toolCardMessage.addFinalPosition(finalPosition);
        return  toolCardMessage;
    }
}
