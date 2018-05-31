package it.polimi.se2018.mvc.controller;

import it.polimi.se2018.mvc.controller.placementlogic.DiePlacerAlone;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.toolcards.*;
import it.polimi.se2018.network.messages.responses.sync.InputResponse;
import it.polimi.se2018.network.messages.responses.sync.ModelViewResponse;
import it.polimi.se2018.network.messages.responses.sync.SyncResponse;
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
    private static final String NO_DIE_IN_HAND = "You haven't a die in your hand!";
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

    @Override
    public void useCard (CopperFoilBurnisher toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        Square squareStart = player.getWindow().getSquare(toolCardMessage.getStartingPosition().get(0));
        Die dieToMove;
        try {
            if (squareStart.isEmpty()) {
                throw new NoDieException();
            }
            dieToMove = squareStart.popDie();
        }
        catch (NoDieException e) {
            throw new ToolCardException("Non hai selezionato un dado");
        }
        try {
            DiePlacerNoValue placer = new DiePlacerNoValue(dieToMove, toolCardMessage.getFinalPosition().get(0), player.getWindow());
            placer.placeDie();
            updateToolCard(toolCardMessage);
            controller.createModelViews(PLAYER + player.getName() + " used Copper Foil Burnisher: \nhe/she moved the die " + dieToMove.getValue() + " " + dieToMove.getColor() + FROM + squareStart.getDescription() + " to " +squareStart.getDescription());
        }
        catch (InvalidPlacementException e) {
            squareStart.setDie(dieToMove);
            throw new ToolCardException(INVALID_POSITION);
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
            controller.createModelViews(PLAYER + player.getName() + " used Cork Backed Straightedge: \nhw/she placed the drafted die on " + toolCardMessage.getFinalPosition().get(0).getDescription());
        }
        catch (InvalidPlacementException e) {
            throw new ToolCardException(INVALID_POSITION);
        }
    }

    @Override
    public void useCard (EglomiseBrush toolCard, ToolCardMessage toolCardMessage)  throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        Die dieToMove;
        Square squareStart = player.getWindow().getSquare(toolCardMessage.getStartingPosition().get(0));
        try {
            if (squareStart.isEmpty()) {
                throw new NoDieException();
            }
            dieToMove = squareStart.popDie();
        }
        catch (NoDieException e) {
            throw new ToolCardException("Non hai selezionato un dado");
        }
        try {
            DiePlacerNoColor placer = new DiePlacerNoColor(dieToMove, toolCardMessage.getFinalPosition().get(0), player.getWindow());
            placer.placeDie();
            updateToolCard(toolCardMessage);
            controller.createModelViews(PLAYER + player.getName() + " used Eglomise Brush: \nhe/she moved the die " + dieToMove.getValue() + " " + dieToMove.getColor() + FROM + squareStart.getDescription() + " to " +squareStart.getDescription());
        }
        catch (InvalidPlacementException e) {
            squareStart.setDie(dieToMove);
            throw new ToolCardException(INVALID_POSITION);
        }
    }

    @Override
    public void useCard (FluxBrush toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        if (!player.hasDieInHand()) {
            throw new ToolCardException(NO_DIE_IN_HAND);
        }
        Die dieToGive = new Die(player.getDieInHand().getValue(), player.getDieInHand().getColor());
        dieToGive.rollDie();
        player.setDieInHand(dieToGive);
        updateToolCard(toolCardMessage);
        controller.createModelViews(PLAYER + player.getName() + " used Flux Brush: \nhe/she re-rolled the drafted die");
    }

    @Override
    public void useCard(FluxRemover toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        try {
            if (!player.hasDieInHand()) {
                throw new ToolCardException(NO_DIE_IN_HAND);
            }
            board.getBag().insertDie(player.getDieInHand());
            player.setDieInHand(board.getBag().extractDie());
            updateToolCard(toolCardMessage);
            board.notify(new InputResponse(toolCardMessage.getPlayerID(), player.getDieInHand().getColor()));
        }
        catch (NoDieException e) {
            throw new ToolCardException("La bag non ha dadi");
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
        controller.createModelViews(PLAYER + player.getName() + " used Glazing Hammer: \nhe/she re-rolled all dice in the draft pool");
    }

    @Override
    public void useCard(GrindingStone toolCard, ToolCardMessage toolCardMessage) throws ToolCardException{
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        if (!player.hasDieInHand()) {
            throw new ToolCardException(NO_DIE_IN_HAND);
        }
        Die dieToGive = new Die(player.getDieInHand().getValue(), player.getDieInHand().getColor());
        dieToGive.flipDie();
        player.setDieInHand(dieToGive);
        updateToolCard(toolCardMessage);
        controller.createModelViews(PLAYER + player.getName() + " used Grinding Stone: \nhe/she flipped the drafted die");
    }

    @Override
    public void useCard(GrozingPliers toolCard, ToolCardMessage toolCardMessage) throws ToolCardException{
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        if (!player.hasDieInHand()) {
            throw new ToolCardException(NO_DIE_IN_HAND);
        }
        if (toolCardMessage.getValue() != +1 && toolCardMessage.getValue() != -1) {
            //This toolcards change the value of the die by +1 or -1, other value are not allowed
            throw  new ToolCardException("Non puoi modificare nel modo indicato il dado! Puoi scegliere solo 0 o 1");
        }
        try {
            Die dieToGive = new Die(player.getDieInHand().getValue(), player.getDieInHand().getColor());
            dieToGive.setValue(dieToGive.getValue() + toolCardMessage.getValue());
            player.setDieInHand(dieToGive);
            updateToolCard(toolCardMessage);
        }
        catch (DieException e) {
            throw new ToolCardException("Il dado assume un valore troppo alto/basso");
        }
        controller.createModelViews(PLAYER + player.getName() + " used GrozingPliers: \nhe/she increased/decresed the drafted die");
    }

    @Override
    public void useCard(Lathekin toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        boolean twoDiceNotCompatible = false; //this check that the two die can be moved together
        boolean diceGoInAdjacentPosition = false; //this cheeck if die go to adjacent position
        Square squareOne = player.getWindow().getSquare(toolCardMessage.getStartingPosition().get(0));
        Square squareTwo = player.getWindow().getSquare((toolCardMessage.getStartingPosition().get(1)));
        Die dieOne = squareOne.popDie();
        Die dieTwo = squareTwo.popDie();
        if (dieOne.getColor() == dieTwo.getColor() || dieOne.getValue() == dieTwo.getValue()) {
            twoDiceNotCompatible = true;
        }
        if (nearPosition(toolCardMessage.getFinalPosition().get(0), toolCardMessage.getFinalPosition().get(1))) {
            diceGoInAdjacentPosition = true;
        }
        if (twoDiceNotCompatible && diceGoInAdjacentPosition) {
            throw new ToolCardException("I due dadi non possono essere spostati insieme");
        }
        try {
            Coordinate finalPositionOne = toolCardMessage.getFinalPosition().get(0);
            Coordinate finalPositionTwo = toolCardMessage.getFinalPosition().get(1);
            DiePlacerNoValue placerOne = new DiePlacerNoValue(dieOne, finalPositionOne, player.getWindow());
            placerOne.placeDie();
            DiePlacerNoValue placerTwo = new DiePlacerNoValue(dieTwo, finalPositionTwo, player.getWindow());
            placerTwo.placeDie();
            updateToolCard(toolCardMessage);
            controller.createModelViews(PLAYER + player.getName() + " used Lathekin: \nhe/she moved the die " + dieOne.getValue() + " " + dieOne.getColor() + FROM + squareOne.getDescription() + " to " + finalPositionOne.getDescription() + " and the die " + dieTwo.getValue() + " " + dieTwo.getColor() + FROM + squareTwo.getDescription() + " to " + finalPositionTwo.getDescription());
        }
        catch (InvalidPlacementException e) {
            squareOne.setDie(dieOne);
            squareTwo.setDie(dieTwo);
            throw new ToolCardException("Uno spostamento selezionato non è valido");
        }
    }

    @Override
    public void useCard(LensCutter toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        if (!player.hasDieInHand()) {
            throw new ToolCardException(NO_DIE_IN_HAND);
        }
        Die dieDrafted = player.getDieInHand();
        Die dieFromRoundTrack = board.getRoundTracker().getDie(toolCardMessage.getRoundTrackerPosition().getRow(), toolCardMessage.getRoundTrackerPosition().getCol());
        player.setDieInHand(dieFromRoundTrack);
        board.getRoundTracker().addToRoundTracker(toolCardMessage.getRoundTrackerPosition().getRow(), dieDrafted);
        updateToolCard(toolCardMessage);
        controller.createModelViews(PLAYER + player.getName() + " used Lens Cutter: \nhw/she swapped the drafted die with the die " + dieFromRoundTrack.getValue() + " " + dieFromRoundTrack.getColor() + " from round track");
    }

    @Override
    public void useCard(RunningPliers toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        if (!board.getRound().isFirstRotation()) {
            throw new ToolCardException("Non è il primo turno, non puoi usare questa Toolcard!");
        }
        if (player.hasDieInHand() || !player.hasDraftedDie()){
            throw new ToolCardException(NO_DIE_IN_HAND);
        }
        player.setHasDraftedDie(false);
        board.getRound().denyNextTurn();
        updateToolCard(toolCardMessage);
        controller.createModelViews(PLAYER + player.getName() + " used Running Pliers");
    }

    @Override
    public void useCard(TapWheel toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        boolean twoDice = true;
        Square squareTwo = null;
        Die dieTwo = null;
        Coordinate finalPositionTwo = null;
        boolean diceGoInAdjacentPosition = false; //this checks if the die can go to adjacent position
        Square squareOne = player.getWindow().getSquare(toolCardMessage.getStartingPosition().get(0));
        Die dieOne = squareOne.popDie();
        Coordinate indexRoundTracker = toolCardMessage.getRoundTrackerPosition();
        Die dieRoundTracker = board.getRoundTracker().getDie(indexRoundTracker.getRow(), indexRoundTracker.getCol());
        if (dieOne.getColor() != dieRoundTracker.getColor()) {
            throw new ToolCardException("I due dadi non sono dello stesso colore del dado sul RoundTracker");
        }
        if (toolCardMessage.getStartingPosition().get(1).equals(new Coordinate(-2, -2))) {
            twoDice = false;
        }
        if (twoDice) {
            squareTwo = player.getWindow().getSquare(toolCardMessage.getStartingPosition().get(1));
            dieTwo = squareTwo.popDie();
            if (dieOne.getColor() != dieTwo.getColor()) {
                throw new ToolCardException("I due dadi non sono dello stesso colore!");
            }
            if (nearPosition(toolCardMessage.getFinalPosition().get(0), toolCardMessage.getFinalPosition().get(1))) {
                diceGoInAdjacentPosition = true;
            }
            if (diceGoInAdjacentPosition) {
                throw new ToolCardException("I due dadi non possono essere spostati insieme");
            }
        }
        try {
            Coordinate finalPositionOne = toolCardMessage.getFinalPosition().get(0);
            DiePlacerNoValue placerOne = new DiePlacerNoValue(dieOne, finalPositionOne, player.getWindow());
            placerOne.placeDie();
            if (twoDice) {
                finalPositionTwo = toolCardMessage.getFinalPosition().get(1);
                DiePlacerNoValue placerTwo = new DiePlacerNoValue(dieTwo, finalPositionTwo, player.getWindow());
                placerTwo.placeDie();
            }
            updateToolCard(toolCardMessage);
            controller.createModelViews(PLAYER + player.getName() + " used Tap Wheel: \nhe/she moved the die " + dieOne.getValue() + " " + dieOne.getColor() + FROM + squareOne.getDescription() + " to " + finalPositionOne.getDescription() + " and the die " + dieTwo.getValue() + " " + dieTwo.getColor() + FROM + squareTwo.getDescription() + " to " + finalPositionTwo.getDescription());
        }
        catch (InvalidPlacementException e) {
            squareOne.setDie(dieOne);
            if (twoDice) squareTwo.setDie(dieTwo);
            throw new ToolCardException("Uno spostamento selezionato non è valido");
        }
    }
}
