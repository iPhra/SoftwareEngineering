package it.polimi.se2018.model.toolcards;

import it.polimi.se2018.controller.ToolCardHandler;
import it.polimi.se2018.utils.exceptions.ToolCardException;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;


public abstract class ToolCard {
    protected final String imagePath;
    protected final String title;
    protected ToolCard(String imagePath, String title) {
        this.imagePath=imagePath;
        this.title=title;
    }

    public abstract void handle(ToolCardHandler handler, ToolCardMessage toolCardMessage) throws ToolCardException;
}
