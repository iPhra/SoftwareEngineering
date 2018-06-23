package it.polimi.se2018.mvc.controller.toolcardcontroller;

import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;

public class RunningPliersController extends ToolCardController {
    public RunningPliersController(Board board) {
        super(board);
    }

    public boolean checkUsability(boolean isUsed, Player player) {
        boolean condition = checkFavorPoints(isUsed, player);
        if(!board.getRound().isFirstRotation()) condition = false;
        if(!player.hasDraftedDie() || (player.hasDraftedDie() && player.hasDieInHand())) condition = false;
        return condition;
    }

    public void useCard(ToolCardMessage toolCardMessage) {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        player.setHasDraftedDie(false);
        board.getRound().denyNextTurn();
        updateToolCard(toolCardMessage);
        board.createModelUpdateResponse(PLAYER + player.getName() + " used Running Pliers and is now playing another turn\n");
    }
}
