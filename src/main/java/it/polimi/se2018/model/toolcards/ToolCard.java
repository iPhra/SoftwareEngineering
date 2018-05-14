package it.polimi.se2018.model.toolcards;

import it.polimi.se2018.controller.ToolCardHandler;
import it.polimi.se2018.utils.exceptions.ToolCardException;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;

import java.util.List;

public abstract class ToolCard {
    protected final String imagePath;
    protected final String title;
    protected List<String> playerRequests;
    protected ToolCard(String imagePath, String title) {
        this.imagePath=imagePath;
        this.title=title;
    }

    public List<String> getPlayerRequests() {
        return playerRequests;
    }

    public abstract void handle(ToolCardHandler handler, ToolCardMessage toolCardMessage) throws ToolCardException;
}
