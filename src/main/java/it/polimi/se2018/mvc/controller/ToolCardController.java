package it.polimi.se2018.mvc.controller;

import it.polimi.se2018.mvc.controller.placementlogic.DiePlacerAlone;
import it.polimi.se2018.mvc.controller.placementlogic.DiePlacerNormal;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.toolcards.*;
import it.polimi.se2018.network.messages.responses.sync.InputResponse;
import it.polimi.se2018.utils.exceptions.*;
import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.controller.placementlogic.DiePlacerNoColor;
import it.polimi.se2018.mvc.controller.placementlogic.DiePlacerNoValue;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used by controller to perform the usage of a toolcard.
 * It's used by a Visitor to understand what method perform
 */
public class ToolCardController implements ToolCardHandler{
    /**
     * String used a lot of times are written here
     */
    private static final String INVALID_POSITION = "The selected position is invalid";
    private static final String PLAYER = "Player ";
    private static final String FROM = " from ";

    /**
     * This is the reference of the board of the game. It's used to call the method to modify it.
     */
    private final Board board;

    ToolCardController(Board board) {
        this.board = board;
    }

    private boolean nearPosition (Coordinate firstPosition, Coordinate secondPosition) {
        return firstPosition.getCol() == secondPosition.getCol() &&
                (firstPosition.getRow() == secondPosition.getRow() + 1 ||
                        firstPosition.getRow() == secondPosition.getRow() - 1) ||
                (firstPosition.getRow() == secondPosition.getRow() &&
                        (firstPosition.getCol() == secondPosition.getCol() + 1 ||
                                firstPosition.getCol() == secondPosition.getCol() - 1));
    }

    private void updateToolCard(ToolCardMessage toolCardMessage) {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        player.setFavorPoints(player.getFavorPoints()-(board.getToolCardsUsage()[toolCardMessage.getToolCardNumber()]? 2:1));
        board.setAlreadyUsed(toolCardMessage.getToolCardNumber());
        player.setHasUsedCard(true);
        player.dropCardInUse();
    }

    private void checkEmptiness(Square squareStart) throws ToolCardException {
        if(squareStart.isEmpty()) throw new ToolCardException("You haven't selected a die\n");
    }

    private void revertSquare(Square squareStart, Die dieToMove) throws ToolCardException {
        squareStart.setDie(dieToMove);
        throw new ToolCardException(INVALID_POSITION);
    }

    @Override
    public void useCard (CopperFoilBurnisher toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        Square squareStart = player.getWindow().getSquare(toolCardMessage.getStartingPosition().get(0));
        checkEmptiness(squareStart);
        Die dieToMove = squareStart.popDie();
        try {
            new DiePlacerNoValue(dieToMove, toolCardMessage.getFinalPosition().get(0), player.getWindow()).placeDie();
            updateToolCard(toolCardMessage);
            board.createWindowResponse(PLAYER + player.getName() + " used Copper Foil Burnisher: \nhe/she moved the die\n " + dieToMove.getValue() + " " + dieToMove.getColor() + FROM + squareStart.getDescription() + " to " +squareStart.getDescription()+"\n",player.getId());
        }
        catch (InvalidPlacementException e) {
            revertSquare(squareStart, dieToMove);
        }
    }

    @Override
    public void useCard (CorkBackedStraightedge toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
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

    @Override
    public void useCard (EglomiseBrush toolCard, ToolCardMessage toolCardMessage)  throws ToolCardException {
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

    @Override
    public void useCard (FluxBrush toolCard, ToolCardMessage toolCardMessage) {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        Die dieToGive = new Die(player.getDieInHand().getValue(), player.getDieInHand().getColor());
        dieToGive.rollDie();
        player.setDieInHand(dieToGive);
        updateToolCard(toolCardMessage);
        board.createModelUpdateResponse(PLAYER + player.getName() + " used Flux Brush: he/she re-rolled the drafted die\n");
    }

    @Override
    public void useCard(FluxRemover toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
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

    @Override
    public void useCard(GlazingHammer toolCard, ToolCardMessage toolCardMessage) {
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

    @Override
    public void useCard(GrindingStone toolCard, ToolCardMessage toolCardMessage) {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        Die dieToGive = new Die(player.getDieInHand().getValue(), player.getDieInHand().getColor());
        dieToGive.flipDie();
        player.setDieInHand(dieToGive);
        updateToolCard(toolCardMessage);
        board.createModelUpdateResponse(PLAYER + player.getName() + " used Grinding Stone: \nhe/she flipped the drafted die\n");
    }

    @Override
    public void useCard(GrozingPliers toolCard, ToolCardMessage toolCardMessage) throws ToolCardException{
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

    @Override
    public void useCard(Lathekin toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        boolean twoDiceNotCompatible = false; //this checks if the two die can be moved together
        boolean diceGoInAdjacentPosition = false; //this checks if die go to adjacent position
        Square squareOne = player.getWindow().getSquare(toolCardMessage.getStartingPosition().get(0));
        Square squareTwo = player.getWindow().getSquare((toolCardMessage.getStartingPosition().get(1)));
        if (squareOne.isEmpty() || squareTwo.isEmpty()) throw new ToolCardException("You don't select two dice");
        Die dieOne = squareOne.popDie();
        Die dieTwo = squareTwo.popDie();
        if (dieOne.getColor() == dieTwo.getColor() || dieOne.getValue() == dieTwo.getValue()) twoDiceNotCompatible = true;
        if (nearPosition(toolCardMessage.getFinalPosition().get(0), toolCardMessage.getFinalPosition().get(1))) diceGoInAdjacentPosition = true;
        if (twoDiceNotCompatible && diceGoInAdjacentPosition) throw new ToolCardException("Both dice can't be moved together\n");
        try {
            new DiePlacerNormal(dieOne, toolCardMessage.getFinalPosition().get(0), player.getWindow()).placeDie();
            new DiePlacerNormal(dieTwo, toolCardMessage.getFinalPosition().get(1), player.getWindow()).placeDie();
            updateToolCard(toolCardMessage);
            board.createWindowResponse(PLAYER + player.getName() + " used Lathekin: \nhe/she moved the die " + dieOne.getValue() + " " + dieOne.getColor() + " and the die " + dieTwo.getValue() + " " + dieTwo.getColor()+"\n",player.getId());
        }
        catch (InvalidPlacementException e) {
            squareOne.setDie(dieOne);
            squareTwo.setDie(dieTwo);
            throw new ToolCardException("Invalid move\n");
        }
    }

    @Override
    public void useCard(LensCutter toolCard, ToolCardMessage toolCardMessage) {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        Die dieDrafted = player.getDieInHand();
        Die dieFromRoundTrack = board.getRoundTracker().popDie(toolCardMessage.getRoundTrackerPosition().getRow(), toolCardMessage.getRoundTrackerPosition().getCol());
        player.setDieInHand(dieFromRoundTrack);
        board.getRoundTracker().addToRoundTracker(toolCardMessage.getRoundTrackerPosition().getRow(), dieDrafted);
        updateToolCard(toolCardMessage);
        board.createRoundTrackerResponse(PLAYER + player.getName() + " used Lens Cutter: \nhw/she swapped the drafted die with the die " + dieFromRoundTrack.getValue() + " " + dieFromRoundTrack.getColor() + " from the Round Tracker\n");
    }

    @Override
    public void useCard(RunningPliers toolCard, ToolCardMessage toolCardMessage) {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        player.setHasDraftedDie(false);
        board.getRound().denyNextTurn();
        updateToolCard(toolCardMessage);
        board.createModelUpdateResponse(PLAYER + player.getName() + " used Running Pliers and is now playing another turn\n");
    }

    @Override
    public void useCard(TapWheel toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        boolean twoDice = toolCardMessage.isCondition();
        Square squareOne = player.getWindow().getSquare(toolCardMessage.getStartingPosition().get(0));
        checkEmptiness(squareOne);
        if(twoDice) checkEmptiness(player.getWindow().getSquare(toolCardMessage.getStartingPosition().get(1)));
        Die dieOne = squareOne.popDie();
        Coordinate roundTrackerIndex = toolCardMessage.getRoundTrackerPosition();
        Die roundTrackerDie = board.getRoundTracker().getDie(roundTrackerIndex.getRow(), roundTrackerIndex.getCol());
        Square squareTwo = null;
        Die dieTwo = null;
        Coordinate finalPositionTwo = null;
        if (dieOne.getColor() != roundTrackerDie.getColor()) {
            squareOne.setDie(dieOne);
            throw new ToolCardException("First die does not match the color on the Round Tracker\n");
        }
        Coordinate finalPositionOne = toolCardMessage.getFinalPosition().get(0);
        if(twoDice){
            squareTwo = player.getWindow().getSquare(toolCardMessage.getStartingPosition().get(1));
            dieTwo = squareTwo.popDie();
        }
        try {
            new DiePlacerNormal(dieOne, finalPositionOne, player.getWindow()).placeDie();
        }
        catch(InvalidPlacementException e) {
            squareOne.setDie(dieOne);
            if (twoDice) squareTwo.setDie(dieTwo);
            throw new ToolCardException("\nFirst die can't be moved there\n");
        }
        if (twoDice) {
            if (dieOne.getColor() != dieTwo.getColor()){
                squareOne.setDie(dieOne);
                squareTwo.setDie(dieTwo);
                player.getWindow().getSquare(finalPositionOne).setDie(null);
                throw new ToolCardException("\nSecond die does not match the color on the Round Tracker\n");
            }
            if (nearPosition(toolCardMessage.getFinalPosition().get(0), toolCardMessage.getFinalPosition().get(1))) {
                squareOne.setDie(dieOne);
                squareTwo.setDie(dieTwo);
                player.getWindow().getSquare(finalPositionOne).setDie(null);
                throw new ToolCardException("\nDice can't be moved together\n");
            }
            finalPositionTwo = toolCardMessage.getFinalPosition().get(1);
            try {
                new DiePlacerNormal(dieTwo, finalPositionTwo, player.getWindow()).placeDie();
            }
            catch(InvalidPlacementException e) {
                squareOne.setDie(dieOne);
                player.getWindow().getSquare(finalPositionOne).setDie(null);
                squareTwo.setDie(dieTwo);
                throw new ToolCardException("\nSecond die can't be moved there\n");
            }
        }
        updateToolCard(toolCardMessage);
        board.createWindowResponse(PLAYER + player.getName() + " used Tap Wheel: \nhe/she moved the die " + dieOne.getValue() + " " + dieOne.getColor() + FROM + squareOne.getDescription() + " to " + finalPositionOne.getDescription() + (twoDice? " and the die " + dieTwo.getValue() + " " + dieTwo.getColor() + FROM + squareTwo.getDescription() + " to " + finalPositionTwo.getDescription()+"\n":""),player.getId());
    }
}
