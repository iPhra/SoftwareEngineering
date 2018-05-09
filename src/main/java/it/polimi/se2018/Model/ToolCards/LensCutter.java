package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Exceptions.NoDieException;
import it.polimi.se2018.Exceptions.ToolCardException;
import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Network.Messages.Requests.ToolCardMessage;

public class LensCutter extends ToolCard {

    public LensCutter(String imagePath, String title, Board board, boolean alreadyUsed) {
        super(imagePath, title, board, alreadyUsed);
    }

    @Override
    //After drafting, swap the drafted die with a die from the Round Track
    public void useCard(ToolCardMessage toolCardMessage) throws ToolCardException {
        if (!toolCardMessage.getPlayer().hasDieInHand()) {
            throw new ToolCardException("Non hai un dado in mano!");
        }
        Die dieDrafted = toolCardMessage.getPlayer().getDieInHand();
        Die dieFromRoundTrack = board.getRoundTracker().getDie(toolCardMessage.getRoundTrackerPosition().get(0).getRow(), toolCardMessage.getRoundTrackerPosition().get(0).getCol());
        toolCardMessage.getPlayer().setDieInHand(dieFromRoundTrack);
        board.getRoundTracker().addToRoundTracker(toolCardMessage.getRoundTrackerPosition().get(0).getRow(), dieDrafted);
    }

    @Override
    public ToolCard setAlreadyUsed() {
        return null;
    }
}
