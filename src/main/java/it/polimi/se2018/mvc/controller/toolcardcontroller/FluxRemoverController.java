package it.polimi.se2018.mvc.controller.toolcardcontroller;

import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.network.messages.responses.sync.modelupdates.InputResponse;
import it.polimi.se2018.utils.exceptions.NoDieException;
import it.polimi.se2018.utils.exceptions.ToolCardException;

public class FluxRemoverController extends ToolCardController {
    public FluxRemoverController(Board board) {
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
            Die die = player.getDieInHand();
            player.setDieInHand(board.getBag().extractDie());
            board.getBag().insertDie(die);
            updateToolCard(toolCardMessage);
            board.notify(new InputResponse(toolCardMessage.getPlayerID(), player.getDieInHand().getColor()));
        }
        catch (NoDieException e) {
            throw new ToolCardException("Bag is empty\n");
        }
    }
}
