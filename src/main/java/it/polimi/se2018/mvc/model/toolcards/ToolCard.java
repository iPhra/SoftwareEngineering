package it.polimi.se2018.mvc.model.toolcards;

import it.polimi.se2018.mvc.controller.ToolCardHandler;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.controller.ToolCardCheckerHandler;
import it.polimi.se2018.utils.exceptions.ChangeActionException;
import it.polimi.se2018.utils.exceptions.HaltException;
import it.polimi.se2018.utils.exceptions.ToolCardException;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.client.view.cli.ToolCardPlayerInputHandler;

import java.io.Serializable;

public abstract class ToolCard implements Serializable {
    private final String title;
    private final String description;

    ToolCard(String title, String description) {
        this.title = title;
        this.description = description;
    }
    private String getTitle() { return title; }

    private String getDescription() { return description; }

    public abstract void handle(ToolCardHandler handler, ToolCardMessage toolCardMessage) throws ToolCardException;

    public abstract ToolCardMessage handleView(ToolCardPlayerInputHandler handler, int toolCardNumber) throws HaltException, ChangeActionException;

    public abstract Boolean handleCheck(ToolCardCheckerHandler handler, boolean isUsed, Player player);

    @Override
    public String toString() {
        return "Title: \"" + getTitle() + "\", Effect: \"" + getDescription() + "\"" + "\n";
    }
}
