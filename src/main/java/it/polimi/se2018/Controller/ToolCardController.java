package it.polimi.se2018.Controller;

import it.polimi.se2018.Exceptions.DieException;
import it.polimi.se2018.Exceptions.InvalidPlacementException;
import it.polimi.se2018.Exceptions.NoDieException;
import it.polimi.se2018.Exceptions.ToolCardException;
import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Model.PlacementLogic.DiePlacerAlone;
import it.polimi.se2018.Model.PlacementLogic.DiePlacerNoColor;
import it.polimi.se2018.Model.PlacementLogic.DiePlacerNoValue;
import it.polimi.se2018.Model.Square;
import it.polimi.se2018.Model.ToolCards.*;
import it.polimi.se2018.Network.Messages.Coordinate;
import it.polimi.se2018.Network.Messages.Requests.ToolCardMessage;

public class ToolCardController implements ToolCardHandler{
    private final Board board;

    public ToolCardController(Board board) {
        this.board=board;
    }

    private boolean nearPosition (Coordinate firstPosition, Coordinate secondPosition) {
        if (firstPosition.getCol() == secondPosition.getCol()) {
            if (firstPosition.getRow() == secondPosition.getRow() + 1 || firstPosition.getRow() == secondPosition.getRow() - 1) {
                return true;
            }
        }
        if (firstPosition.getRow() == secondPosition.getRow()) {
            if (firstPosition.getCol() == secondPosition.getCol() + 1 || firstPosition.getCol() == secondPosition.getCol() - 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void useCard (CopperFoilBurnisher toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        try {
            Square squareStart = toolCardMessage.getPlayer().getMap().getSquare(toolCardMessage.getStartingPosition().get(0));
            if (squareStart.isEmpty()) {
                throw new NoDieException();
            }
            Die dieToMove = squareStart.getDie();
            DiePlacerNoValue placer = new DiePlacerNoValue(dieToMove, toolCardMessage.getFinalPosition().get(0), toolCardMessage.getPlayer().getMap());
            placer.placeDie();
            squareStart.setDie(null);
        }
        catch (NoDieException e) {
            throw new ToolCardException("Non hai selezionato un dado");
        }
        catch (InvalidPlacementException e) {
            throw new ToolCardException("La posizione scelta non è valida");
        }
    }

    @Override
    public void useCard (CorkBackedStraightedge toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        try {
            Die dieToPlace = toolCardMessage.getPlayer().getDieInHand();
            DiePlacerAlone placer = new DiePlacerAlone(dieToPlace, toolCardMessage.getFinalPosition().get(0), toolCardMessage.getPlayer().getMap());
            placer.placeDie();
        }
        catch (InvalidPlacementException e) {
            throw new ToolCardException("La posizione scelta non è valida");
        }
    }

    @Override
    public void useCard (EglomiseBrush toolCard, ToolCardMessage toolCardMessage)  throws ToolCardException {
        try {
            Square squareStart = toolCardMessage.getPlayer().getMap().getSquare(toolCardMessage.getStartingPosition().get(0));
            if (squareStart.isEmpty()) {
                throw new NoDieException();
            }
            Die dieToMove = squareStart.getDie();
            DiePlacerNoColor placer = new DiePlacerNoColor(dieToMove, toolCardMessage.getFinalPosition().get(0), toolCardMessage.getPlayer().getMap());
            placer.placeDie();
            squareStart.setDie(null);
        }
        catch (NoDieException e) {
            throw new ToolCardException("Non hai selezionato un dado");
        }
        catch (InvalidPlacementException e) {
            throw new ToolCardException("La posizione scelta non è valida");
        }
    }

    @Override
    public void useCard (FluxBrush toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        if (!toolCardMessage.getPlayer().hasDieInHand()) {
            throw new ToolCardException("Non hai un dado in mano!");
        }
        toolCardMessage.getPlayer().getDieInHand().rollDie();
    }

    @Override
    public void useCard(FluxRemover toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        try {
            if (!toolCardMessage.getPlayer().hasDieInHand()) {
                throw new ToolCardException("Non hai un dado in mano!");
            }
            board.getBag().insertDie(toolCardMessage.getPlayer().getDieInHand());
            toolCardMessage.getPlayer().setDieInHand(board.getBag().extractDie());
            //darò il colore alla view
            toolCardMessage.getPlayer().getDieInHand().setValue(toolCardMessage.getValue());
            //getValue prende il valore che desidera il giocatore attraverso moveMessage (?)
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
    }

    @Override
    public void useCard(GrindingStone toolCard, ToolCardMessage toolCardMessage) throws ToolCardException{
        if (!toolCardMessage.getPlayer().hasDieInHand()) {
            throw new ToolCardException("Non hai un dado in mano!");
        }
        toolCardMessage.getPlayer().getDieInHand().flipDie();
    }

    @Override
    public void useCard(GrozingPliers toolCard, ToolCardMessage toolCardMessage) throws ToolCardException{
        if (!toolCardMessage.getPlayer().hasDieInHand()) {
            throw new ToolCardException("Non hai un dado in mano!");
        }
        if (toolCardMessage.getValue() != 1 || toolCardMessage.getValue() != -1) {
            //This toolcard change the value of the die by +1 or -1, other value are not allowed
            throw  new ToolCardException("Non puoi modificare nel modo indicato il dado! Puoi scegliere solo +1 o -1");
        }
        try {
            Die dieToChange = toolCardMessage.getPlayer().getDieInHand();
            dieToChange.setValue(dieToChange.getValue() + toolCardMessage.getValue());
        }
        catch (DieException e) {
            throw new ToolCardException("Il dado assume un valore troppo alto/basso");
        }
    }

    @Override
    public void useCard(Lathekin toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        boolean twoDiceNotCompatible = false; //this check that the two die can be moved together
        boolean diceGoInAdjacentPosition = false; //this cheeck if die go to adjacent position
        Square squareOne = toolCardMessage.getPlayer().getMap().getSquare(toolCardMessage.getStartingPosition().get(0));
        Square squareTwo = toolCardMessage.getPlayer().getMap().getSquare((toolCardMessage.getStartingPosition().get(1)));
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
            DiePlacerNoValue placerOne = new DiePlacerNoValue(dieOne, toolCardMessage.getFinalPosition().get(0), toolCardMessage.getPlayer().getMap());
            placerOne.placeDie();
            DiePlacerNoValue placerTwo = new DiePlacerNoValue(dieTwo, toolCardMessage.getFinalPosition().get(1), toolCardMessage.getPlayer().getMap());
            placerTwo.placeDie();
            squareOne.setDie(null);
            squareTwo.setDie(null);
        }
        catch (InvalidPlacementException e) {
            throw new ToolCardException("Uno spostamento selezionato non è valido");
        }
    }

    @Override
    public void useCard(LensCutter toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        if (!toolCardMessage.getPlayer().hasDieInHand()) {
            throw new ToolCardException("Non hai un dado in mano!");
        }
        Die dieDrafted = toolCardMessage.getPlayer().getDieInHand();
        Die dieFromRoundTrack = board.getRoundTracker().getDie(toolCardMessage.getRoundTrackerPosition().get(0).getRow(), toolCardMessage.getRoundTrackerPosition().get(0).getCol());
        toolCardMessage.getPlayer().setDieInHand(dieFromRoundTrack);
        board.getRoundTracker().addToRoundTracker(toolCardMessage.getRoundTrackerPosition().get(0).getRow(), dieDrafted);
    }

    @Override
    public void useCard(RunningPliers toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        if (!board.getRound().isFirstRotation()) {
            throw new ToolCardException("Non è il primo turno, non puoi usare questa Toolcard!");
        }
        if (toolCardMessage.getPlayer().hasDieInHand() || !toolCardMessage.getPlayer().hasDraftedDie()){
            throw new ToolCardException("Non hai un dado in mano!");
        }
        toolCardMessage.getPlayer().setHasDraftedDie(false);
        board.getRound().denyNextTurn();
    }

    @Override
    public void useCard(TapWheel toolCard, ToolCardMessage toolCardMessage) throws ToolCardException {
        boolean diceGoInAdjacentPosition = false; //this cheeck if die go to adjacent position
        Square squareOne = toolCardMessage.getPlayer().getMap().getSquare(toolCardMessage.getStartingPosition().get(0));
        Square squareTwo = toolCardMessage.getPlayer().getMap().getSquare(toolCardMessage.getStartingPosition().get(1));
        Die dieOne = squareOne.getDie();
        Die dieTwo = squareTwo.getDie();
        Coordinate indexRoundTracker = toolCardMessage.getRoundTrackerPosition().get(0);
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
            DiePlacerNoValue placerOne = new DiePlacerNoValue(dieOne, toolCardMessage.getFinalPosition().get(0), toolCardMessage.getPlayer().getMap());
            placerOne.placeDie();
            DiePlacerNoValue placerTwo = new DiePlacerNoValue(dieTwo, toolCardMessage.getFinalPosition().get(1), toolCardMessage.getPlayer().getMap());
            placerTwo.placeDie();
            squareOne.setDie(null);
            squareTwo.setDie(null);
        }
        catch (InvalidPlacementException e) {
            throw new ToolCardException("Uno spostamento selezionato non è valido");
        }
    }

}
