package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Exceptions.DieException;
import it.polimi.se2018.Exceptions.NoDieException;
import it.polimi.se2018.Exceptions.ToolCardException;
import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Messages.ToolCardMessage;

public class GrindingStone extends ToolCard {

    public GrindingStone(String imagePath, String title, Board board, boolean alreadyUsed) {
        super(imagePath, title, board, alreadyUsed);
    }

    @Override
    //After drafting, flip the die to its opposite side
    public void useCard(ToolCardMessage toolCardMessage) throws ToolCardException{
        try {
            if (!toolCardMessage.getPlayer().hasDieInHand()) {
                throw new ToolCardException();
            }
            toolCardMessage.getPlayer().getDieInHand().flipDie();
        }
        catch (NoDieException e) {
            throw new ToolCardException();
        }
    }

    @Override
    public ToolCard setAlreadyUsed() {
        return null;
    }
}

