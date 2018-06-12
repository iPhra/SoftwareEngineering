package it.polimi.se2018.network.messages.responses.sync.modelupdates;

import it.polimi.se2018.mvc.controller.ModelView;
import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.PrivateObjective;
import it.polimi.se2018.mvc.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;
import it.polimi.se2018.network.messages.responses.sync.SyncResponse;
import it.polimi.se2018.network.messages.responses.sync.SyncResponseHandler;

import java.util.Arrays;
import java.util.List;

/**
 * This is the class containing a response from the Server containing an updated copy of the state of the game
 */
public class ModelViewResponse extends SyncResponse {
    /**
     * This is the object containing the updated copy of the model
     */
    private final ModelView modelView;
    private final List<ToolCard> toolCards;
    private final PrivateObjective privateObjective;
    private final List<PublicObjective> publicObjectives;

    public ModelViewResponse(Board board, ModelView modelView, int playerID) {
        super(playerID);
        this.modelView = modelView;
        privateObjective = board.getPlayerByID(playerID).getPrivateObjective();
        toolCards = Arrays.asList(board.getToolCards());
        publicObjectives = Arrays.asList(board.getPublicObjectives());
    }

    public List<ToolCard> getToolCards() {
        return toolCards;
    }

    public PrivateObjective getPrivateObjective() {
        return privateObjective;
    }

    public List<PublicObjective> getPublicObjectives() {
        return publicObjectives;
    }

    /**
     * @return the updated copy of the model
     */
    public ModelView getModelView() {
        return modelView;
    }

    /**
     * Uses the handler to handle this specific {@link ModelView} response
     * @param syncResponseHandler is the object who will handle this response
     */
    @Override
    public void handle(SyncResponseHandler syncResponseHandler) { syncResponseHandler.handleResponse(this);}


}
