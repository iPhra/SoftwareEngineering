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
    private final List<PublicObjective> publicObjectives;

    /**
     * These are the {@link ToolCard} extracted on the Board
     */
    private final List<ToolCard> toolCards;

    /**
     * This is the {@link PrivateObjective} belonging to the player this message will be sent to
     */
    private final PrivateObjective privateObjective;

    private final int playersNumber;


    public ReconnectionResponse(int playerID, ModelView modelView, PrivateObjective privateObjective, List<PublicObjective> publicObjectives, List<ToolCard> toolCards, int playersNumber) {
        super(playerID);
        this.modelView = modelView;
        this.privateObjective = privateObjective;
        this.publicObjectives = publicObjectives;
        this.toolCards = toolCards;
        this.playersNumber=playersNumber;
    }

    public int getPlayersNumber() {
        return playersNumber;
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
