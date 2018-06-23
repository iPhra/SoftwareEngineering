package it.polimi.se2018.mvc.controller.toolcardcontroller;

import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.utils.exceptions.DieException;
import it.polimi.se2018.utils.exceptions.ToolCardException;

public class GrozingPliersController extends ToolCardController {
    public GrozingPliersController(Board board) {
        super(board);
    }

    public boolean checkUsability(boolean isUsed, Player player) {
        boolean condition = checkFavorPoints(isUsed, player);
        if(!player.hasDieInHand()) condition = false;
        return condition;
    }

    public void useCard(ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        try {
            Die dieToGive = new Die(player.getDieInHand().getValue(), player.getDieInHand().getColor());
            dieToGive.setValue(dieToGive.getValue() + (toolCardMessage.isCondition()? 1:-1));
            player.setDieInHand(dieToGive);
            updateToolCard(toolCardMessage);
        }
        catch (DieException e) {
            throw new ToolCardException("Invalid value, you can't increase a 6 or decrease a 1\n");
        }
        board.createModelUpdateResponse(PLAYER + player.getName() + " used GrozingPliers: \nhe/she increased/decresed the drafted die\n");
    }
}
