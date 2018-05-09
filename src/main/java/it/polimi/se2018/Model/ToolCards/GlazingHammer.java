package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Network.Messages.Requests.ToolCardMessage;

public class GlazingHammer extends ToolCard {

    public GlazingHammer(String imagePath, String title, Board board, boolean alreadyUsed) {
        super(imagePath, title,board, alreadyUsed);
    }

    @Override
    //Re-roll all dice in the Draft Pool
    public void useCard(ToolCardMessage toolCardMessage) {
        for(Die die : board.getDraftPool().getAllDice()) {
            die.rollDie();
        }
    }

    @Override
    public ToolCard setAlreadyUsed() {
        return null;
    }
}
