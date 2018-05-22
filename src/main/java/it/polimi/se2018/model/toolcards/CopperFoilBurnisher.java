package it.polimi.se2018.model.toolcards;

import it.polimi.se2018.controller.ToolCardHandler;
import it.polimi.se2018.model.Board;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.ToolCardCheckerHandler;
import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.utils.exceptions.TimeOutException;
import it.polimi.se2018.utils.exceptions.ToolCardException;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.client.view.cli.ToolCardPlayerInputHandler;

public class CopperFoilBurnisher extends ToolCard {

    public CopperFoilBurnisher() {
        super("Copper Foil Burnisher", "Move any one die in your window ignoring value restrictions");
    }

    @Override
    public Response handle(ToolCardHandler handler, ToolCardMessage message) throws ToolCardException{
        return handler.useCard(this, message);
    }

    @Override
    public ToolCardMessage handleView(ToolCardPlayerInputHandler handler, int toolcardnumber) throws TimeOutException {
        return handler.getPlayerRequests(this, toolcardnumber);
    }

    @Override
    public Boolean handleCheck(ToolCardCheckerHandler handler, boolean isUsed, Player player, Board board){
        return handler.checkUsability(this, isUsed, player, board);
    }
}

