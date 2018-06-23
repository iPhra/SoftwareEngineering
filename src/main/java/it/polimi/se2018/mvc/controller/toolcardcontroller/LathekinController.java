package it.polimi.se2018.mvc.controller.toolcardcontroller;

import it.polimi.se2018.mvc.controller.placementlogic.DiePlacerNoValue;
import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.utils.exceptions.InvalidPlacementException;
import it.polimi.se2018.utils.exceptions.ToolCardException;

public class LathekinController extends ToolCardController {
    public LathekinController(Board board) {
        super(board);
    }

    public boolean checkUsability(boolean isUsed, Player player) {
        boolean condition = checkFavorPoints(isUsed, player);
        //because you need to check if 2 dice exist: if yes, there are 18 empty slots or less
        if (player.getWindow().countEmptySlots() > 18) condition = false;
        return condition;
    }

    public void useCard(ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        boolean twoDiceNotCompatible = false; //this checks if the two die can be moved together
        boolean diceGoInAdjacentPosition = false; //this checks if die go to adjacent position
        Square squareOne = player.getWindow().getSquare(toolCardMessage.getStartingPosition().get(0));
        Square squareTwo = player.getWindow().getSquare((toolCardMessage.getStartingPosition().get(1)));
        Die dieOne = squareOne.popDie();
        Die dieTwo = squareTwo.popDie();
        if (dieOne.getColor() == dieTwo.getColor() || dieOne.getValue() == dieTwo.getValue()) twoDiceNotCompatible = true;
        if (nearPosition(toolCardMessage.getFinalPosition().get(0), toolCardMessage.getFinalPosition().get(1))) diceGoInAdjacentPosition = true;
        if (twoDiceNotCompatible && diceGoInAdjacentPosition) throw new ToolCardException("Both dice can't be moved together\n");
        try {
            new DiePlacerNoValue(dieOne, toolCardMessage.getFinalPosition().get(0), player.getWindow()).placeDie();
            new DiePlacerNoValue(dieTwo, toolCardMessage.getFinalPosition().get(1), player.getWindow()).placeDie();
            updateToolCard(toolCardMessage);
            board.createWindowResponse(PLAYER + player.getName() + " used Lathekin: \nhe/she moved the die " + dieOne.getValue() + " " + dieOne.getColor() + " and the die " + dieTwo.getValue() + " " + dieTwo.getColor()+"\n",player.getId());
        }
        catch (InvalidPlacementException e) {
            squareOne.setDie(dieOne);
            squareTwo.setDie(dieTwo);
            throw new ToolCardException("Invalid move\n");
        }
    }
}
