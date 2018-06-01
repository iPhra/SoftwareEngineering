package it.polimi.se2018.network.messages.responses.sync;

import it.polimi.se2018.mvc.controller.ModelView;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.PrivateObjective;
import it.polimi.se2018.mvc.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;

import java.util.List;

public class ReconnectionResponse extends SyncResponse {
    private ModelView modelView;

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

    /**
     * This is the number of players in the current match
     */
    private int playersNumber;

    private final boolean windowsChosen;

    public ReconnectionResponse(int playerID, boolean windowsChosen) {
        super(playerID);
        this.windowsChosen = windowsChosen;
    }

    public boolean isWindowsChosen() {
        return windowsChosen;
    }

    public void setModelView(ModelView modelView) {
        this.modelView = modelView;
    }


    public void setPublicObjectives(List<PublicObjective> publicObjectives) {
        this.publicObjectives = publicObjectives;
    }

    public void setToolCards(List<ToolCard> toolCards) {
        this.toolCards = toolCards;
    }

    public void setPrivateObjective(PrivateObjective privateObjective) {
        this.privateObjective = privateObjective;
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
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
     * @param syncResponseHandler is the object who will handle this response
     */
    @Override
    public void handle(SyncResponseHandler syncResponseHandler) {
        syncResponseHandler.handleResponse(this);
    }
}
