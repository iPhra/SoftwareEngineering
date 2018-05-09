package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Exceptions.InvalidPlacementException;
import it.polimi.se2018.Exceptions.NoDieException;
import it.polimi.se2018.Exceptions.ToolCardException;
import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Model.Messages.ToolCardMessage;
import it.polimi.se2018.Model.PlacementLogic.DiePlacerNoValue;

public class CopperFoilBurnisher extends ToolCard {

    public CopperFoilBurnisher(String imagePath, String title, Board board, boolean alreadyUsed) {
        super(imagePath, title, board,alreadyUsed);
        //SETTARE USABLEAFTERDRAFT
    }

    @Override
    //Move any one die in your window ignoring value restrictions
    public void useCard(ToolCardMessage toolCardMessage) throws ToolCardException {
        try {
            Die dieToMove = toolCardMessage.getPlayer().getMap().getDie(toolCardMessage.getStartingPosition().get(0));
            DiePlacerNoValue placer = new DiePlacerNoValue(dieToMove, toolCardMessage.getFinalPosition().get(0), toolCardMessage.getPlayer().getMap());
            placer.placeDie();
            toolCardMessage.getPlayer().getMap().popDie(toolCardMessage.getStartingPosition().get(0));
        }
        catch (NoDieException e) {
            throw new ToolCardException();
        }
        catch (InvalidPlacementException e) {
            throw new ToolCardException();
        }
    }

    @Override
    public ToolCard setAlreadyUsed() {
        return null;
    }
}

