package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Messages.ToolCardMessage;

public class GrindingStone extends ToolCard {

    public GrindingStone(String imagePath, String title, Board board, boolean alreadyUsed) {
        super(imagePath, title, board, alreadyUsed);
    }

    @Override
    public void useCard(ToolCardMessage toolCardMessage) {}

    @Override
    public ToolCard setAlreadyUsed() {
        return null;
    }
}

