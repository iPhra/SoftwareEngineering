package it.polimi.se2018.network.messages.responses;

import it.polimi.se2018.model.Window;
import it.polimi.se2018.model.objectives.privateobjectives.PrivateObjective;
import it.polimi.se2018.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.model.toolcards.ToolCard;

import java.util.List;

public class SetupResponse extends Response{
    private final List<Window> windows;
    private final List<PublicObjective> publicObjectives;
    private final List<ToolCard> toolCards;
    private final PrivateObjective privateObjective;

    public SetupResponse(int player, List<Window> windows, List<PublicObjective> publicObjectives, PrivateObjective privateObjective, List<ToolCard> toolCards) {
        super(player);
        this.windows = windows;
        this.privateObjective = privateObjective;
        this.publicObjectives = publicObjectives;
        this.toolCards = toolCards;
    }

    public List<Window> getWindows() {
        return windows;
    }

    public List<PublicObjective> getPublicObjectives() {
        return publicObjectives;
    }

    public List<ToolCard> getToolCards() {
        return toolCards;
    }

    public PrivateObjective getPrivateObjective() {
        return privateObjective;
    }

    @Override
    public void handle(ResponseHandler handler){
        handler.handleResponse(this);
    }
}
