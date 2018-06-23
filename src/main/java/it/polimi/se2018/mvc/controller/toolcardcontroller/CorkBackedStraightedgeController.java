package it.polimi.se2018.mvc.controller.toolcardcontroller;

import it.polimi.se2018.mvc.controller.placementlogic.DiePlacerAlone;
import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.utils.exceptions.InvalidPlacementException;
import it.polimi.se2018.utils.exceptions.ToolCardException;

public class CorkBackedStraightedgeController extends ToolCardController {
    public CorkBackedStraightedgeController(Board board) {
        super(board);
    }

    public boolean checkUsability(boolean isUsed, Player player) {
        boolean condition = checkFavorPoints(isUsed, player);
        if(!player.hasDieInHand()) condition = false;
        return condition;
    }

    public void useCard (ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        try {
            Die dieToPlace = player.getDieInHand();
            DiePlacerAlone placer = new DiePlacerAlone(dieToPlace, toolCardMessage.getFinalPosition().get(0), player.getWindow());
            placer.placeDie();
            player.dropDieInHand();
            updateToolCard(toolCardMessage);
            board.createWindowResponse(PLAYER + player.getName() + " used Cork Backed Straightedge: \nhe/she placed the drafted die on " + toolCardMessage.getFinalPosition().get(0).getDescription()+"\n",player.getId());
        }
        catch (InvalidPlacementException e) {
            throw new ToolCardException(INVALID_POSITION);
        }
    }
}
