package it.polimi.se2018.model.toolcards;

import it.polimi.se2018.controller.ToolCardHandler;
import it.polimi.se2018.utils.exceptions.ToolCardException;
import it.polimi.se2018.model.Board;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;

public class GrindingStone extends ToolCard {

    public GrindingStone(String imagePath, String title, Board board, boolean alreadyUsed) {
        super(imagePath, title, board, alreadyUsed);
    }

    @Override
    public void handle(ToolCardHandler handler, ToolCardMessage message) throws ToolCardException{
        handler.useCard(this, message);
    }

    @Override
    public ToolCard setAlreadyUsed() {
        return new GrindingStone(imagePath, title, board, true);
    }
}

