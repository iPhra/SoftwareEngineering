package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Exceptions.InvalidPlacementException;
import it.polimi.se2018.Exceptions.NoDieException;
import it.polimi.se2018.Exceptions.ToolCardException;
import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Network.Messages.Requests.ToolCardMessage;
import it.polimi.se2018.Model.PlacementLogic.DiePlacerAlone;

public class CorkBackedStraightedge extends ToolCard {

    public CorkBackedStraightedge(String imagePath, String title, Board board, boolean alreadyUsed) {
        super(imagePath, title, board,alreadyUsed);
    }

    @Override
    //After drafting, place the die in a spot that is not adjacent to another die
    public void useCard(ToolCardMessage toolCardMessage) throws ToolCardException {
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
    public ToolCard setAlreadyUsed() {
        return null;
    }
}
