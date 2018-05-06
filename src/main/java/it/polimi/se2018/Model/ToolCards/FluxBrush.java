package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Exceptions.ToolCardException;
import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Messages.ToolCardMessage;

public class FluxBrush extends ToolCard {

    public FluxBrush(String imagePath, String title, Board board, boolean alreadyUsed) {
        super(imagePath, title, board,alreadyUsed);
    }
    @Override
    //After drafting, re-roll the drafted die
    public void useCard(ToolCardMessage toolCardMessage) throws ToolCardException {
        if (!toolCardMessage.getPlayer().hasDieInHand()) {
            throw new ToolCardException();
        }
        toolCardMessage.getPlayer().getDieInHand().rollDie();
    }

    @Override
    public ToolCard setAlreadyUsed() {
        return null;
    }
}


