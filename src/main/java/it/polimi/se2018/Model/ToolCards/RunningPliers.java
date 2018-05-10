package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Exceptions.ToolCardException;
import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Network.Messages.Requests.ToolCardMessage;

public class RunningPliers extends ToolCard {

    public RunningPliers(String imagePath, String title, Board board, boolean alreadyUsed) {
        super(imagePath, title, board, alreadyUsed);
    }

    @Override
    //After your first turn, immediately draft a die
    public void useCard(ToolCardMessage toolCardMessage) throws ToolCardException {
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
    public ToolCard setAlreadyUsed() {
        return null;
    }
}

