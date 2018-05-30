package it.polimi.se2018.network.messages.responses;

import it.polimi.se2018.mvc.controller.ModelView;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.PrivateObjective;
import it.polimi.se2018.mvc.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;

import java.util.List;

public class ReconnectionResponse extends Response {
    private final ModelView modelView;

    /**
     * These are the {@link PublicObjective} extracted on the Board
     */
    private List<PublicObjective> publicObjectives;

    /**
     * These are the {@link ToolCard} extracted on the Board
     */
    private List<ToolCard> toolCards;

    /**
     * This is the {@link PrivateObjective} belonging to the player this message will be sent to
     */
    private PrivateObjective privateObjective;


    public ReconnectionResponse(int playerID, ModelView modelView, PrivateObjective privateObjective, List<ToolCard> toolCards, List<PublicObjective> publicObjectives ) {
        super(playerID);
        this.modelView = modelView;
        this.privateObjective = privateObjective;
        this.publicObjectives = publicObjectives;
        this.toolCards = toolCards;
    }

    public ModelView getModelView() {
        return modelView;
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

    /**
     * Uses the handler to handle this specific disconnection response
     *
     * @param responseHandler is the object who will handle this response
     */
    @Override
    public void handle(ResponseHandler responseHandler) {
        responseHandler.handleResponse(this);
    }
}
