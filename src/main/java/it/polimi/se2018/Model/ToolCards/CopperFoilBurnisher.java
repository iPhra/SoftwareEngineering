package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Exceptions.InvalidPlacementException;
import it.polimi.se2018.Exceptions.NoDieException;
import it.polimi.se2018.Exceptions.ToolCardException;
import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Network.Messages.Requests.ToolCardMessage;
import it.polimi.se2018.Model.PlacementLogic.DiePlacerNoValue;
import it.polimi.se2018.Model.Square;

public class CopperFoilBurnisher extends ToolCard {

    public CopperFoilBurnisher(String imagePath, String title, Board board, boolean alreadyUsed) {
        super(imagePath, title, board,alreadyUsed);
        //SETTARE USABLEAFTERDRAFT
    }

    @Override
    //Move any one die in your window ignoring value restrictions
    public void useCard(ToolCardMessage toolCardMessage) throws ToolCardException {
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
    public ToolCard setAlreadyUsed() {
        return null;
    }
}

