package it.polimi.se2018.model.toolcards;

import it.polimi.se2018.controller.ToolCardHandler;
import it.polimi.se2018.utils.exceptions.ToolCardException;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;

public class EglomiseBrush extends ToolCard {

    public EglomiseBrush(String imagePath, String title, boolean alreadyUsed) {
        super(imagePath, title, alreadyUsed);
    }

    @Override
    public void handle(ToolCardHandler handler, ToolCardMessage message) throws ToolCardException{
        handler.useCard(this, message);
    }

    @Override
    public ToolCard setAlreadyUsed() {
        return new EglomiseBrush(imagePath, title,true);
    }
}
