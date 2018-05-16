package it.polimi.se2018.model.toolcards;

import it.polimi.se2018.controller.ToolCardHandler;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.utils.exceptions.ToolCardException;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.view.cli.CLIClientView;

public class CopperFoilBurnisher extends ToolCard {

    public CopperFoilBurnisher(String imagePath) {
        super(imagePath, "Copper Foil Burnisher", "Move any one die in your window ignoring value restrictions");
    }

    @Override
    public void handle(ToolCardHandler handler, ToolCardMessage message) throws ToolCardException{
        handler.useCard(this, message);
    }

    public ToolCardMessage getPlayerRequests(int playerID, CLIClientView clientView, int toolcardnumber) {
        Coordinate startingCoordinate = clientView.getDieInMap();
        Coordinate finalPosition = clientView.getCoordinate();
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, toolcardnumber);
        toolCardMessage.addStartingPosition(startingCoordinate);
        toolCardMessage.addFinalPosition(finalPosition);
        return  toolCardMessage;
    }
}

