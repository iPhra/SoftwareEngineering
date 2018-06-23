package it.polimi.se2018.mvc.controller.toolcardcontroller;

import it.polimi.se2018.mvc.controller.placementlogic.DiePlacerNoValue;
import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.utils.exceptions.InvalidPlacementException;
import it.polimi.se2018.utils.exceptions.ToolCardException;

public class TapWheelController extends ToolCardController {
    public TapWheelController(Board board) {
        super(board);
    }

    public boolean checkUsability(boolean isUsed, Player player) {
        if(!checkFavorPoints(isUsed, player)) return false;
        //you need to check if two dice exist: if so there are 18 empty slots or less left
        for(Square square: player.getWindow()) {
            if(!square.isEmpty() && board.getRoundTracker().containsColor(square.getDie().getColor())) return true;
        }
        return false;
    }

    public void useCard(ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        boolean twoDice = toolCardMessage.isCondition();
        Square squareOne = player.getWindow().getSquare(toolCardMessage.getStartingPosition().get(0));
        Die dieOne = squareOne.popDie();
        Coordinate roundTrackerIndex = toolCardMessage.getRoundTrackerPosition();
        Die roundTrackerDie = board.getRoundTracker().getDie(roundTrackerIndex.getRow(), roundTrackerIndex.getCol());
        if (dieOne.getColor() != roundTrackerDie.getColor()) throw new ToolCardException("First die does not match the color on the Round Tracker\n");
        Coordinate finalPositionOne = toolCardMessage.getFinalPosition().get(0);
        try {
            new DiePlacerNoValue(dieOne, finalPositionOne, player.getWindow()).placeDie();
        }
        catch(InvalidPlacementException e) {
            squareOne.setDie(dieOne);
            throw new ToolCardException("\nFirst die can't be moved there\n");
        }
        Square squareTwo = null;
        Die dieTwo = null;
        Coordinate finalPositionTwo = null;
        if (twoDice) {
            squareTwo = player.getWindow().getSquare(toolCardMessage.getStartingPosition().get(1));
            dieTwo = squareTwo.popDie();
            if (dieOne.getColor() != dieTwo.getColor())throw new ToolCardException("\nSecond die does not match the color on the Round Tracker\n");
            if (nearPosition(toolCardMessage.getFinalPosition().get(0), toolCardMessage.getFinalPosition().get(1))) throw new ToolCardException("\nDice can't be moved together\n");
            finalPositionTwo = toolCardMessage.getFinalPosition().get(1);
            try {
                new DiePlacerNoValue(dieTwo, finalPositionTwo, player.getWindow()).placeDie();
            }
            catch(InvalidPlacementException e) {
                squareTwo.setDie(dieTwo);
                throw new ToolCardException("\nSecond die can't be moved there\n");
            }
        }
        updateToolCard(toolCardMessage);
        board.createWindowResponse(PLAYER + player.getName() + " used Tap Wheel: \nhe/she moved the die " + dieOne.getValue() + " " + dieOne.getColor() + FROM + squareOne.getDescription() + " to " + finalPositionOne.getDescription() + (twoDice? " and the die " + dieTwo.getValue() + " " + dieTwo.getColor() + FROM + squareTwo.getDescription() + " to " + finalPositionTwo.getDescription()+"\n":""),player.getId());
    }
}
