package it.polimi.se2018.controller;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.network.messages.responses.InputResponse;
import it.polimi.se2018.network.messages.responses.ModelViewResponse;
import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.utils.exceptions.*;
import it.polimi.se2018.model.Board;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.controller.placementlogic.DiePlacerAlone;
import it.polimi.se2018.controller.placementlogic.DiePlacerNoColor;
import it.polimi.se2018.controller.placementlogic.DiePlacerNoValue;
import it.polimi.se2018.model.Square;
import it.polimi.se2018.model.toolcards.*;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;

public class ToolCardController implements ToolCardHandler{
    private static final String NO_DIE_IN_HAND = "You haven't a die in your hand!";
    private static final String INVALID_POSITION = "The selected position is invalid";
    private final Board board;

    ToolCardController(Board board) {
        this.board=board;
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
    public Response useCard (CopperFoilBurnisher toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
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
            squareStart.setDie(null);
            updateToolCard(toolCardMessage);
            return new ModelViewResponse(board.modelViewCopy());
        }
        catch (InvalidPlacementException e) {
            squareStart.setDie(dieToMove);
            throw new ToolCardException(INVALID_POSITION);
        }
    }

    @Override
    public Response useCard (CorkBackedStraightedge toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        try {
            Die dieToPlace = player.getDieInHand();
            DiePlacerAlone placer = new DiePlacerAlone(dieToPlace, toolCardMessage.getFinalPosition().get(0), player.getWindow());
            placer.placeDie();
            player.dropDieInHand();
            updateToolCard(toolCardMessage);
            return new ModelViewResponse(board.modelViewCopy());
        }
        catch (InvalidPlacementException e) {
            throw new ToolCardException(INVALID_POSITION);
        }
    }

    @Override
    public Response useCard (EglomiseBrush toolCard, ToolCardMessage toolCardMessage)  throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        Die dieToMove;
        Square squareStart = player.getWindow().getSquare(toolCardMessage.getStartingPosition().get(0));
        try {
            if (squareStart.isEmpty()) {
                throw new NoDieException();
            }
            dieToMove = squareStart.getDie();
        }
        catch (NoDieException e) {
            throw new ToolCardException("Non hai selezionato un dado");
        }
        try {
            DiePlacerNoColor placer = new DiePlacerNoColor(dieToMove, toolCardMessage.getFinalPosition().get(0), player.getWindow());
            placer.placeDie();
            squareStart.setDie(null);
            updateToolCard(toolCardMessage);
            return new ModelViewResponse(board.modelViewCopy());
        }
        catch (InvalidPlacementException e) {
            throw new ToolCardException(INVALID_POSITION);
        }
    }

    @Override
    public Response useCard (FluxBrush toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        if (!player.hasDieInHand()) {
            throw new ToolCardException(NO_DIE_IN_HAND);
        }
        player.getDieInHand().rollDie();
        updateToolCard(toolCardMessage);
        return new ModelViewResponse(board.modelViewCopy());
    }

    @Override
    public Response useCard(FluxRemover toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        try {
            if (!player.hasDieInHand()) {
                throw new ToolCardException(NO_DIE_IN_HAND);
            }
            board.getBag().insertDie(player.getDieInHand());
            player.setDieInHand(board.getBag().extractDie());
            updateToolCard(toolCardMessage);
            return new InputResponse(toolCardMessage.getPlayerID(), player.getDieInHand().getColor());
        }
        catch (NoDieException e) {
            throw new ToolCardException("La bag non ha dadi");
        }
    }

    @Override
    public Response useCard(GlazingHammer toolCard, ToolCardMessage toolCardMessage) {
        for(Die die : board.getDraftPool().getAllDice()) {
            die.rollDie();
        }
        updateToolCard(toolCardMessage);
        return new ModelViewResponse(board.modelViewCopy());
    }

    @Override
    public Response useCard(GrindingStone toolCard, ToolCardMessage toolCardMessage) throws ToolCardException{
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        if (!player.hasDieInHand()) {
            throw new ToolCardException(NO_DIE_IN_HAND);
        }
        player.getDieInHand().flipDie();
        updateToolCard(toolCardMessage);
        return new ModelViewResponse(board.modelViewCopy());
    }

    @Override
    public Response useCard(GrozingPliers toolCard, ToolCardMessage toolCardMessage) throws ToolCardException{
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        if (!player.hasDieInHand()) {
            throw new ToolCardException(NO_DIE_IN_HAND);
        }
        if (toolCardMessage.getValue() != +1 && toolCardMessage.getValue() != -1) {
            //This toolcards change the value of the die by +1 or -1, other value are not allowed
            throw  new ToolCardException("Non puoi modificare nel modo indicato il dado! Puoi scegliere solo 0 o 1");
        }
        try {
            Die dieToChange = player.getDieInHand();
            dieToChange.setValue(dieToChange.getValue() + toolCardMessage.getValue());
            updateToolCard(toolCardMessage);
        }
        catch (DieException e) {
            throw new ToolCardException("Il dado assume un valore troppo alto/basso");
        }
        ModelView copy = board.modelViewCopy();
        return new ModelViewResponse(copy);
    }

    @Override
    public Response useCard(Lathekin toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
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
            DiePlacerNoValue placerOne = new DiePlacerNoValue(dieOne, toolCardMessage.getFinalPosition().get(0), player.getWindow());
            placerOne.placeDie();
            DiePlacerNoValue placerTwo = new DiePlacerNoValue(dieTwo, toolCardMessage.getFinalPosition().get(1), player.getWindow());
            placerTwo.placeDie();
            updateToolCard(toolCardMessage);
            return new ModelViewResponse(board.modelViewCopy());
        }
        catch (InvalidPlacementException e) {
            squareOne.setDie(dieOne);
            squareTwo.setDie(dieTwo);
            throw new ToolCardException("Uno spostamento selezionato non è valido");
        }
    }

    @Override
    public Response useCard(LensCutter toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        if (!player.hasDieInHand()) {
            throw new ToolCardException(NO_DIE_IN_HAND);
        }
        Die dieDrafted = player.getDieInHand();
        Die dieFromRoundTrack = board.getRoundTracker().getDie(toolCardMessage.getRoundTrackerPosition().getRow(), toolCardMessage.getRoundTrackerPosition().getCol());
        player.setDieInHand(dieFromRoundTrack);
        board.getRoundTracker().addToRoundTracker(toolCardMessage.getRoundTrackerPosition().getRow(), dieDrafted);
        updateToolCard(toolCardMessage);
        return new ModelViewResponse(board.modelViewCopy());
    }

    @Override
    public Response useCard(RunningPliers toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
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
        return new ModelViewResponse(board.modelViewCopy());
    }

    @Override
    public Response useCard(TapWheel toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        boolean diceGoInAdjacentPosition = false; //this cheeck if die go to adjacent position
        Square squareOne = player.getWindow().getSquare(toolCardMessage.getStartingPosition().get(0));
        Square squareTwo = player.getWindow().getSquare(toolCardMessage.getStartingPosition().get(1));
        Die dieOne = squareOne.popDie();
        Die dieTwo = squareTwo.popDie();
        Coordinate indexRoundTracker = toolCardMessage.getRoundTrackerPosition();
        Die dieRoundTracker = board.getRoundTracker().getDie(indexRoundTracker.getRow(), indexRoundTracker.getCol());
        if (dieOne.getColor() != dieTwo.getColor()) {
            throw new ToolCardException("I due dadi non sono dello stesso colore!");
        }
        if (dieTwo.getColor() != dieRoundTracker.getColor()) {
            throw new ToolCardException("I due dadi non sono dello stesso colore del dado sul RoundTracker");
        }
        if (nearPosition(toolCardMessage.getFinalPosition().get(0), toolCardMessage.getFinalPosition().get(1))) {
            diceGoInAdjacentPosition = true;
        }
        if (diceGoInAdjacentPosition) {
            throw new ToolCardException("I due dadi non possono essere spostati insieme");
        }
        try {
            DiePlacerNoValue placerOne = new DiePlacerNoValue(dieOne, toolCardMessage.getFinalPosition().get(0), player.getWindow());
            placerOne.placeDie();
            DiePlacerNoValue placerTwo = new DiePlacerNoValue(dieTwo, toolCardMessage.getFinalPosition().get(1), player.getWindow());
            placerTwo.placeDie();
            updateToolCard(toolCardMessage);
            return new ModelViewResponse(board.modelViewCopy());
        }
        catch (InvalidPlacementException e) {
            squareOne.setDie(dieOne);
            squareTwo.setDie(dieTwo);
            throw new ToolCardException("Uno spostamento selezionato non è valido");
        }
    }
}
