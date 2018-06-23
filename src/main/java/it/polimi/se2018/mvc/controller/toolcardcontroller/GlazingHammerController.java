package it.polimi.se2018.mvc.controller.toolcardcontroller;

import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;

import java.util.ArrayList;
import java.util.List;

public class GlazingHammerController extends ToolCardController {
    public GlazingHammerController(Board board) {
        super(board);
    }

    public boolean checkUsability(boolean isUsed, Player player) {
        boolean condition = checkFavorPoints(isUsed, player);
        if(board.getRound().isFirstRotation()) condition = false;
        if(player.hasDraftedDie()) condition = false;
        return condition;
    }

    public void useCard(ToolCardMessage toolCardMessage) {
        List<Die> diceReRolled = new ArrayList<>();
        for(Die die : board.getDraftPool().getAllDice()) {
            Die dieToGive = new Die(die.getValue(), die.getColor());
            dieToGive.rollDie();
            diceReRolled.add(dieToGive);
        }
        board.getDraftPool().fillDraftPool(diceReRolled);
        updateToolCard(toolCardMessage);
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        board.createDraftPoolResponse(PLAYER + player.getName() + " used Glazing Hammer: \nhe/she re-rolled all dice in the draft pool\n");
    }
}
