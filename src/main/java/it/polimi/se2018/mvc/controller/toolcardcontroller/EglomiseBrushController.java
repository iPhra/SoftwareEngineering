package it.polimi.se2018.mvc.controller.toolcardcontroller;

import it.polimi.se2018.mvc.controller.placementlogic.DiePlacerNoColor;
import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.utils.exceptions.InvalidPlacementException;
import it.polimi.se2018.utils.exceptions.ToolCardException;

public class EglomiseBrushController extends ToolCardController {
    public EglomiseBrushController(Board board) {
        super(board);
    }

    public boolean checkUsability(boolean isUsed, Player player) {
        boolean condition = checkFavorPoints(isUsed, player);
        //you need to check if 1 die exists: if so, there are 19 empty slots or less
        if(player.getWindow().countEmptySlots()>19) condition = false;
        return condition;
    }

    public void useCard (ToolCardMessage toolCardMessage)  throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        Square squareStart = player.getWindow().getSquare(toolCardMessage.getStartingPosition().get(0));
        checkEmptiness(squareStart);
        Die dieToMove = squareStart.popDie();
        try {
            new DiePlacerNoColor(dieToMove, toolCardMessage.getFinalPosition().get(0), player.getWindow()).placeDie();
            updateToolCard(toolCardMessage);
            board.createWindowResponse(PLAYER + player.getName() + " used Eglomise Brush: he/she moved the die\n " + dieToMove.getValue() + " " + dieToMove.getColor() + FROM + squareStart.getDescription() + " to " +squareStart.getDescription(),player.getId());
        }
        catch (InvalidPlacementException e) {
            revertSquare(squareStart, dieToMove);
        }
    }
}
