package it.polimi.se2018.mvc.model.toolcards;

import it.polimi.se2018.mvc.controller.ToolCardHandler;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.controller.ToolCardCheckerHandler;
import it.polimi.se2018.network.messages.responses.sync.SyncResponse;
import it.polimi.se2018.utils.exceptions.HaltException;
import it.polimi.se2018.utils.exceptions.ToolCardException;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.client.view.cli.ToolCardPlayerInputHandler;

import java.io.Serializable;

public abstract class ToolCard implements Serializable {
    protected final String title;
    protected final String description;

    protected ToolCard(String title, String description) {
        this.title = title;
        this.description = description;
    }
    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public abstract void handle(ToolCardHandler handler, ToolCardMessage toolCardMessage) throws ToolCardException;

    public abstract ToolCardMessage handleView(ToolCardPlayerInputHandler handler, int toolCardNumber) throws HaltException;

    public abstract Boolean handleCheck(ToolCardCheckerHandler handler, boolean isUsed, Player player);

    @Override
    public String toString() {
        return "Title: \"" + getTitle() + "\", Effect: \"" + getDescription() + "\"" + "\n";
    }
}
