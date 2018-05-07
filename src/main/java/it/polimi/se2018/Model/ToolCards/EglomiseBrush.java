package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Exceptions.InvalidPlacementException;
import it.polimi.se2018.Exceptions.ToolCardException;
import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Model.Messages.ToolCardMessage;
import it.polimi.se2018.Model.PlacementLogic.DiePlacerNoColor;

public class EglomiseBrush extends ToolCard {

    public EglomiseBrush(String imagePath, String title, Board board, boolean alreadyUsed) {
        super(imagePath, title, board, alreadyUsed);
    }

    @Override
    //Move any die in your window ignoring color restrictions
    public void useCard(ToolCardMessage toolCardMessage) throws ToolCardException {
        try {
            Die dieToMove = toolCardMessage.getPlayer().getMap().getDie(toolCardMessage.getStartingPosition().get(0));
            DiePlacerNoColor placer = new DiePlacerNoColor(dieToMove, toolCardMessage.getFinalPosition().get(0), toolCardMessage.getPlayer().getMap());
            placer.placeDie();
            toolCardMessage.getPlayer().getMap().popDie(toolCardMessage.getStartingPosition().get(0));
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
