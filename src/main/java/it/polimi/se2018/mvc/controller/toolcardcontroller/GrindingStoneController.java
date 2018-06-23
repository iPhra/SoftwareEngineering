package it.polimi.se2018.mvc.controller.toolcardcontroller;

import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;

public class GrindingStoneController extends ToolCardController {
    public GrindingStoneController(Board board) {
        super(board);
    }

    public boolean checkUsability(boolean isUsed, Player player) {
        boolean condition = checkFavorPoints(isUsed, player);
        if(!player.hasDieInHand()) condition = false;
        return condition;
    }

    public void useCard(ToolCardMessage toolCardMessage) {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        Die dieToGive = new Die(player.getDieInHand().getValue(), player.getDieInHand().getColor());
        dieToGive.flipDie();
        player.setDieInHand(dieToGive);
        updateToolCard(toolCardMessage);
        board.createModelUpdateResponse(PLAYER + player.getName() + " used Grinding Stone: \nhe/she flipped the drafted die\n");
    }
}
