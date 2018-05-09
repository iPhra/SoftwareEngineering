package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Exceptions.InvalidPlacementException;
import it.polimi.se2018.Exceptions.NoDieException;
import it.polimi.se2018.Exceptions.ToolCardException;
import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Model.Messages.Coordinate;
import it.polimi.se2018.Model.Messages.ToolCardMessage;
import it.polimi.se2018.Model.PlacementLogic.DiePlacerNoValue;
import it.polimi.se2018.Model.Square;

public class Lathekin extends ToolCard {

    public Lathekin(String imagePath, String title, Board board, boolean alreadyUsed) {
        super(imagePath, title, board, alreadyUsed);
    }

    //da rivedere
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
    //Move exactly two dice, obeying all placement restrictions
    public void useCard(ToolCardMessage toolCardMessage) throws ToolCardException {
        boolean twoDiceNotCompatible = false; //this check that the two die can be moved together
        boolean diceGoInAdjacentPosition = false; //this cheeck if die go to adjacent position
        Square squareOne = toolCardMessage.getPlayer().getMap().getSquare(toolCardMessage.getStartingPosition().get(0));
        Square squareTwo = toolCardMessage.getPlayer().getMap().getSquare((toolCardMessage.getStartingPosition().get(1)));
        Die dieOne = squareOne.getDie();
        Die dieTwo = squareTwo.getDie();
        //TODO ti ho modificato popDie in getDie, ricorda di aggiungerlo
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
    public ToolCard setAlreadyUsed() {
        return null;
    }
}