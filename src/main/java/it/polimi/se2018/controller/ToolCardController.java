package it.polimi.se2018.controller;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.exceptions.DieException;
import it.polimi.se2018.utils.exceptions.InvalidPlacementException;
import it.polimi.se2018.utils.exceptions.NoDieException;
import it.polimi.se2018.utils.exceptions.ToolCardException;
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
        Player player = board.getPlayerByIndex(toolCardMessage.getPlayerID());
        player.setFavorPoints(player.getFavorPoints()-(board.getToolCardsUsage()[toolCardMessage.getToolCardNumber()]? 2:1));
        board.setAlreadyUsed(toolCardMessage.getToolCardNumber());
        player.setHasUsedCard(true);
        player.dropCardInUse();
    }

    @Override
    public void useCard (CopperFoilBurnisher toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByIndex(toolCardMessage.getPlayerID());
        try {
            Square squareStart = player.getMap().getSquare(toolCardMessage.getStartingPosition().get(0));
            if (squareStart.isEmpty()) {
                throw new NoDieException();
            }
            Die dieToMove = squareStart.getDie();
            DiePlacerNoValue placer = new DiePlacerNoValue(dieToMove, toolCardMessage.getFinalPosition().get(0), player.getMap());
            placer.placeDie();
            squareStart.setDie(null);
            updateToolCard(toolCardMessage);
        }
        catch (NoDieException e) {
            throw new ToolCardException("Non hai selezionato un dado");
        }
        catch (InvalidPlacementException e) {
            throw new ToolCardException(INVALID_POSITION);
        }
    }

    @Override
    public void useCard (CorkBackedStraightedge toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByIndex(toolCardMessage.getPlayerID());
        try {
            Die dieToPlace = player.getDieInHand();
            DiePlacerAlone placer = new DiePlacerAlone(dieToPlace, toolCardMessage.getFinalPosition().get(0), player.getMap());
            placer.placeDie();
            updateToolCard(toolCardMessage);
        }
        catch (InvalidPlacementException e) {
            throw new ToolCardException(INVALID_POSITION);
        }
    }

    @Override
    public void useCard (EglomiseBrush toolCard, ToolCardMessage toolCardMessage)  throws ToolCardException {
        Player player = board.getPlayerByIndex(toolCardMessage.getPlayerID());
        try {
            Square squareStart = player.getMap().getSquare(toolCardMessage.getStartingPosition().get(0));
            if (squareStart.isEmpty()) {
                throw new NoDieException();
            }
            Die dieToMove = squareStart.getDie();
            DiePlacerNoColor placer = new DiePlacerNoColor(dieToMove, toolCardMessage.getFinalPosition().get(0), player.getMap());
            placer.placeDie();
            squareStart.setDie(null);
            updateToolCard(toolCardMessage);
        }
        catch (NoDieException e) {
            throw new ToolCardException("Non hai selezionato un dado");
        }
        catch (InvalidPlacementException e) {
            throw new ToolCardException(INVALID_POSITION);
        }
    }

    @Override
    public void useCard (FluxBrush toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByIndex(toolCardMessage.getPlayerID());
        if (!player.hasDieInHand()) {
            throw new ToolCardException(NO_DIE_IN_HAND);
        }
        player.getDieInHand().rollDie();
        updateToolCard(toolCardMessage);
    }

    @Override
    public void useCard(FluxRemover toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByIndex(toolCardMessage.getPlayerID());
        try {
            if (!player.hasDieInHand()) {
                throw new ToolCardException(NO_DIE_IN_HAND);
            }
            board.getBag().insertDie(player.getDieInHand());
            player.setDieInHand(board.getBag().extractDie());
            //darò il colore alla view
            player.getDieInHand().setValue(toolCardMessage.getValue());
            //getValue prende il valore che desidera il giocatore attraverso moveMessage (?)
            updateToolCard(toolCardMessage);
        }
        catch (NoDieException e) {
            throw new ToolCardException("La bag non ha dadi");
        }
        catch (DieException e) {
            throw new ToolCardException("Valore del dado non valido!");
        }
    }

    @Override
    public void useCard(GlazingHammer toolCard, ToolCardMessage toolCardMessage) {
        for(Die die : board.getDraftPool().getAllDice()) {
            die.rollDie();
        }
        updateToolCard(toolCardMessage);
    }

    @Override
    public void useCard(GrindingStone toolCard, ToolCardMessage toolCardMessage) throws ToolCardException{
        Player player = board.getPlayerByIndex(toolCardMessage.getPlayerID());
        if (player.hasDieInHand()) {
            throw new ToolCardException(NO_DIE_IN_HAND);
        }
        player.getDieInHand().flipDie();
        updateToolCard(toolCardMessage);
    }

    @Override
    public void useCard(GrozingPliers toolCard, ToolCardMessage toolCardMessage) throws ToolCardException{
        Player player = board.getPlayerByIndex(toolCardMessage.getPlayerID());
        if (!player.hasDieInHand()) {
            throw new ToolCardException(NO_DIE_IN_HAND);
        }
        if (toolCardMessage.getValue() != 1 || toolCardMessage.getValue() != -1) {
            //This toolcards change the value of the die by +1 or -1, other value are not allowed
            throw  new ToolCardException("Non puoi modificare nel modo indicato il dado! Puoi scegliere solo +1 o -1");
        }
        try {
            Die dieToChange = player.getDieInHand();
            dieToChange.setValue(dieToChange.getValue() + toolCardMessage.getValue());
            updateToolCard(toolCardMessage);
        }
        catch (DieException e) {
            throw new ToolCardException("Il dado assume un valore troppo alto/basso");
        }
    }

    @Override
    public void useCard(Lathekin toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByIndex(toolCardMessage.getPlayerID());
        boolean twoDiceNotCompatible = false; //this check that the two die can be moved together
        boolean diceGoInAdjacentPosition = false; //this cheeck if die go to adjacent position
        Square squareOne = player.getMap().getSquare(toolCardMessage.getStartingPosition().get(0));
        Square squareTwo = player.getMap().getSquare((toolCardMessage.getStartingPosition().get(1)));
        Die dieOne = squareOne.getDie();
        Die dieTwo = squareTwo.getDie();
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
            DiePlacerNoValue placerOne = new DiePlacerNoValue(dieOne, toolCardMessage.getFinalPosition().get(0), player.getMap());
            placerOne.placeDie();
            DiePlacerNoValue placerTwo = new DiePlacerNoValue(dieTwo, toolCardMessage.getFinalPosition().get(1), player.getMap());
            placerTwo.placeDie();
            squareOne.setDie(null);
            squareTwo.setDie(null);
            updateToolCard(toolCardMessage);
        }
        catch (InvalidPlacementException e) {
            throw new ToolCardException("Uno spostamento selezionato non è valido");
        }
    }

    @Override
    public void useCard(LensCutter toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByIndex(toolCardMessage.getPlayerID());
        if (!player.hasDieInHand()) {
            throw new ToolCardException(NO_DIE_IN_HAND);
        }
        Die dieDrafted = player.getDieInHand();
        Die dieFromRoundTrack = board.getRoundTracker().getDie(toolCardMessage.getRoundTrackerPosition().getRow(), toolCardMessage.getRoundTrackerPosition().getCol());
        player.setDieInHand(dieFromRoundTrack);
        board.getRoundTracker().addToRoundTracker(toolCardMessage.getRoundTrackerPosition().getRow(), dieDrafted);
        updateToolCard(toolCardMessage);
    }

    @Override
    public void useCard(RunningPliers toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByIndex(toolCardMessage.getPlayerID());
        if (!board.getRound().isFirstRotation()) {
            throw new ToolCardException("Non è il primo turno, non puoi usare questa Toolcard!");
        }
        if (player.hasDieInHand() || !player.hasDraftedDie()){
            throw new ToolCardException(NO_DIE_IN_HAND);
        }
        player.setHasDraftedDie(false);
        board.getRound().denyNextTurn();
        updateToolCard(toolCardMessage);
    }

    @Override
    public void useCard(TapWheel toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        Player player = board.getPlayerByIndex(toolCardMessage.getPlayerID());
        boolean diceGoInAdjacentPosition = false; //this cheeck if die go to adjacent position
        Square squareOne = player.getMap().getSquare(toolCardMessage.getStartingPosition().get(0));
        Square squareTwo = player.getMap().getSquare(toolCardMessage.getStartingPosition().get(1));
        Die dieOne = squareOne.getDie();
        Die dieTwo = squareTwo.getDie();
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
            DiePlacerNoValue placerOne = new DiePlacerNoValue(dieOne, toolCardMessage.getFinalPosition().get(0), player.getMap());
            placerOne.placeDie();
            DiePlacerNoValue placerTwo = new DiePlacerNoValue(dieTwo, toolCardMessage.getFinalPosition().get(1), player.getMap());
            placerTwo.placeDie();
            squareOne.setDie(null);
            squareTwo.setDie(null);
            updateToolCard(toolCardMessage);
        }
        catch (InvalidPlacementException e) {
            throw new ToolCardException("Uno spostamento selezionato non è valido");
        }
    }

}
