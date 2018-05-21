package it.polimi.se2018.model.toolcards;

import it.polimi.se2018.controller.ToolCardHandler;
import it.polimi.se2018.model.Board;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.ToolCardCheckerHandler;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.client.view.cli.ToolCardPlayerInputHandler;

public class GlazingHammer extends ToolCard {

    public GlazingHammer() {
        super("Glazing Hammer",
                "Re-roll all dice in the Draft Pool, (you can only in first turn of the round and before drafting)");
    }

    @Override
    public Response handle(ToolCardHandler handler, ToolCardMessage message) {
        return handler.useCard(this, message);
    }

    @Override
    public ToolCardMessage handleView(ToolCardPlayerInputHandler handler, int toolcardnumber) {
        return handler.getPlayerRequests(this, toolcardnumber);
    }

    @Override
    public Boolean handleCheck(ToolCardCheckerHandler handler, boolean isUsed, Player player, Board board){
        return handler.checkUsability(this, isUsed, player, board);
    }
}
