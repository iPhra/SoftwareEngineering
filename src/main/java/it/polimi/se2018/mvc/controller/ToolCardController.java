package it.polimi.se2018.mvc.controller;

import it.polimi.se2018.mvc.controller.placementlogic.DiePlacerAlone;
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

public class ToolCardController implements ToolCardHandler{
    private static final String INVALID_POSITION = "The selected position is invalid";
    private static final String PLAYER = "Player ";
    private static final String FROM = " from ";
    private final Board board;
    private final Controller controller;

    ToolCardController(Board board, Controller controller) {
        this.board = board;
        this.controller = controller;
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
        if(squareStart.isEmpty()) throw new ToolCardException("You haven't selected a die");
    }

    private void revertSquare(Square squareStart, Die dieToMove) throws ToolCardException {
        squareStart.setDie(dieToMove);
        throw new ToolCardException(INVALID_POSITION);
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
            board.createModelViews(PLAYER + player.getName() + " used Cork Backed Straightedge: \nhw/she placed the drafted die on " + toolCardMessage.getFinalPosition().get(0).getDescription());
        }
        catch (InvalidPlacementException e) {
            throw new ToolCardException(INVALID_POSITION);
        }
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
            board.createModelViews(PLAYER + player.getName() + " used Copper Foil Burnisher: \nhe/she moved the die " + dieToMove.getValue() + " " + dieToMove.getColor() + FROM + squareStart.getDescription() + " to " +squareStart.getDescription());
        }
        catch (InvalidPlacementException e) {
            revertSquare(squareStart, dieToMove);
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
            board.createModelViews(PLAYER + player.getName() + " used Eglomise Brush: \nhe/she moved the die " + dieToMove.getValue() + " " + dieToMove.getColor() + FROM + squareStart.getDescription() + " to " +squareStart.getDescription());
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
        board.createModelViews(PLAYER + player.getName() + " used Flux Brush: \nhe/she re-rolled the drafted die");
    }

    @Override
    public void useCard(FluxRemover toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        board.getBag().insertDie(player.getDieInHand());
        try {
            player.setDieInHand(board.getBag().extractDie());
            updateToolCard(toolCardMessage);
            board.notify(new InputResponse(toolCardMessage.getPlayerID(), player.getDieInHand().getColor()));
        }
        catch (NoDieException e) {
            throw new ToolCardException("Bag is empty");
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
        board.createModelViews(PLAYER + player.getName() + " used Glazing Hammer: \nhe/she re-rolled all dice in the draft pool");
    }

    @Override
    public void useCard(GrindingStone toolCard, ToolCardMessage toolCardMessage) {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        Die dieToGive = new Die(player.getDieInHand().getValue(), player.getDieInHand().getColor());
        dieToGive.flipDie();
        player.setDieInHand(dieToGive);
        updateToolCard(toolCardMessage);
        board.createModelViews(PLAYER + player.getName() + " used Grinding Stone: \nhe/she flipped the drafted die");
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
            throw new ToolCardException("Invalid value, you can't increase a 6 or decrease a 1");
        }
        board.createModelViews(PLAYER + player.getName() + " used GrozingPliers: \nhe/she increased/decresed the drafted die");
    }

    @Override
    public void useCard(LensCutter toolCard, ToolCardMessage toolCardMessage) {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        Die dieDrafted = player.getDieInHand();
        Die dieFromRoundTrack = board.getRoundTracker().getDie(toolCardMessage.getRoundTrackerPosition().getRow(), toolCardMessage.getRoundTrackerPosition().getCol());
        player.setDieInHand(dieFromRoundTrack);
        board.getRoundTracker().addToRoundTracker(toolCardMessage.getRoundTrackerPosition().getRow(), dieDrafted);
        updateToolCard(toolCardMessage);
        board.createModelViews(PLAYER + player.getName() + " used Lens Cutter: \nhw/she swapped the drafted die with the die " + dieFromRoundTrack.getValue() + " " + dieFromRoundTrack.getColor() + " from round track");
    }

    @Override
    public void useCard(RunningPliers toolCard, ToolCardMessage toolCardMessage) {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        player.setHasDraftedDie(false);
        board.getRound().denyNextTurn();
        updateToolCard(toolCardMessage);
        board.createModelViews(PLAYER + player.getName() + " used Running Pliers");
    }

    @Override
    public void useCard(Lathekin toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        boolean twoDiceNotCompatible = false; //this checks that the two die can be moved together
        boolean diceGoInAdjacentPosition = false; //this checks if die go to adjacent position
        Square squareOne = player.getWindow().getSquare(toolCardMessage.getStartingPosition().get(0));
        Square squareTwo = player.getWindow().getSquare((toolCardMessage.getStartingPosition().get(1)));
        Die dieOne = squareOne.popDie();
        Die dieTwo = squareTwo.popDie();
        if (dieOne.getColor() == dieTwo.getColor() || dieOne.getValue() == dieTwo.getValue()) twoDiceNotCompatible = true;
        if (nearPosition(toolCardMessage.getFinalPosition().get(0), toolCardMessage.getFinalPosition().get(1))) diceGoInAdjacentPosition = true;
        if (twoDiceNotCompatible && diceGoInAdjacentPosition) throw new ToolCardException("Both dice can't be moved together");
        try {
            new DiePlacerNoValue(dieOne, toolCardMessage.getFinalPosition().get(0), player.getWindow()).placeDie();
            new DiePlacerNoValue(dieTwo, toolCardMessage.getFinalPosition().get(1), player.getWindow()).placeDie();
            updateToolCard(toolCardMessage);
            board.createModelViews(PLAYER + player.getName() + " used Lathekin: \nhe/she moved the die " + dieOne.getValue() + " " + dieOne.getColor() + " and the die " + dieTwo.getValue() + " " + dieTwo.getColor());
        }
        catch (InvalidPlacementException e) {
            squareOne.setDie(dieOne);
            squareTwo.setDie(dieTwo);
            throw new ToolCardException("Invalid move");
        }
    }

    @Override
    public void useCard(TapWheel toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        boolean twoDice = toolCardMessage.isCondition();
        Square squareOne = player.getWindow().getSquare(toolCardMessage.getStartingPosition().get(0));
        Die dieOne = squareOne.popDie();
        Coordinate roundTrackerIndex = toolCardMessage.getRoundTrackerPosition();
        Die roundTrackerDie = board.getRoundTracker().getDie(roundTrackerIndex.getRow(), roundTrackerIndex.getCol());
        if (dieOne.getColor() != roundTrackerDie.getColor()) throw new ToolCardException("First die does not match the color on the Round Tracker");
        Coordinate finalPositionOne = toolCardMessage.getFinalPosition().get(0);
        try {
            new DiePlacerNoValue(dieOne, finalPositionOne, player.getWindow()).placeDie();
        }
        catch(InvalidPlacementException e) {
            squareOne.setDie(dieOne);
            throw new ToolCardException("First die can't be moved there");
        }
        Square squareTwo = player.getWindow().getSquare(toolCardMessage.getStartingPosition().get(1));
        Die dieTwo = squareTwo.popDie();
        if (dieOne.getColor() != dieTwo.getColor())throw new ToolCardException("Second die does not match the color on the Round Tracker");
        if (nearPosition(toolCardMessage.getFinalPosition().get(0), toolCardMessage.getFinalPosition().get(1))) throw new ToolCardException("Dice can't be moved together");
        Coordinate finalPositionTwo = toolCardMessage.getFinalPosition().get(1);
        if (twoDice) {
            try {
                new DiePlacerNoValue(dieTwo, finalPositionTwo, player.getWindow()).placeDie();
            }
            catch(InvalidPlacementException e) {
                squareTwo.setDie(dieTwo);
                throw new ToolCardException("Second die can't be moved there");
            }
        }
        updateToolCard(toolCardMessage);
        board.createModelViews(PLAYER + player.getName() + " used Tap Wheel: \nhe/she moved the die " + dieOne.getValue() + " " + dieOne.getColor() + FROM + squareOne.getDescription() + " to " + finalPositionOne.getDescription() + " and the die " + dieTwo.getValue() + " " + dieTwo.getColor() + FROM + squareTwo.getDescription() + " to " + finalPositionTwo.getDescription());
    }
}
