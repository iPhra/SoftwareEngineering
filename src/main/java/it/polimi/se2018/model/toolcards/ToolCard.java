package it.polimi.se2018.model.toolcards;

import it.polimi.se2018.controller.ToolCardHandler;
import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.utils.exceptions.FluxRemoverException;
import it.polimi.se2018.utils.exceptions.ToolCardException;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.view.cli.ToolCardPlayerInputHandler;

import java.util.List;

public abstract class ToolCard {
    protected final String title;
    protected final String description;
    protected List<String> playerRequests;
    protected List<String> conditionRequests;

    protected ToolCard(String title, String description) {
        this.title = title;
        this.description = description;
    }
    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public List<String> getPlayerRequests() {
        return playerRequests;
    }

    public List<String> getConditionRequests() {
        return conditionRequests;
    }

    public abstract Response handle(ToolCardHandler handler, ToolCardMessage toolCardMessage) throws ToolCardException;

    public abstract ToolCardMessage handleView(ToolCardPlayerInputHandler handler, int toolcardnumber);
}
