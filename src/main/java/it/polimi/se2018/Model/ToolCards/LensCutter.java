package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Exceptions.NoDieException;
import it.polimi.se2018.Exceptions.ToolCardException;
import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Model.Messages.ToolCardMessage;

public class LensCutter extends ToolCard {

    public LensCutter(String imagePath, String title, Board board, boolean alreadyUsed) {
        super(imagePath, title, board, alreadyUsed);
    }

    @Override
    public void useCard(ToolCardMessage toolCardMessage) throws ToolCardException {
        if (!toolCardMessage.getPlayer().hasDieInHand()) {
            throw new ToolCardException();
        }
        try {
            Die dieDrafted = toolCardMessage.getPlayer().getDieInHand();
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
