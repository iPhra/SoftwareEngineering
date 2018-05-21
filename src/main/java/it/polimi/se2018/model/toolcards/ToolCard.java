package it.polimi.se2018.model.toolcards;

import it.polimi.se2018.controller.ToolCardHandler;
import it.polimi.se2018.model.Board;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.ToolCardCheckerHandler;
import it.polimi.se2018.network.messages.responses.Response;
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

    public abstract Response handle(ToolCardHandler handler, ToolCardMessage toolCardMessage) throws ToolCardException;

    public abstract ToolCardMessage handleView(ToolCardPlayerInputHandler handler, int toolCardNumber);

    public abstract Boolean handleCheck(ToolCardCheckerHandler handler, boolean isUsed, Player player, Board board);
}
