package it.polimi.se2018.network.messages.responses.sync;

import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.PrivateObjective;
import it.polimi.se2018.mvc.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;

import java.util.List;

/**
 * This is the class containing the setup of the game, sent by the Server to the Client
 * @author Francesco Lorenzo
 */
public class SetupResponse extends SyncResponse {
    /**
     * These are the {@link Window} a user can choose from
     */
    private final List<Window> windows;

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

    public SetupResponse(int player, List<Window> windows, List<PublicObjective> publicObjectives, PrivateObjective privateObjective, List<ToolCard> toolCards, int playersNumber) {
        super(player);
        this.windows = windows;
        this.privateObjective = privateObjective;
        this.publicObjectives = publicObjectives;
        this.toolCards = toolCards;
        this.playersNumber = playersNumber;
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    /**
     * @return a {@link List} of {@link Window} a user can choose from
     */
    public List<Window> getWindows() {
        return windows;
    }

    /**
     * @return the {@link PublicObjective} in the game
     */
    public List<PublicObjective> getPublicObjectives() {
        return publicObjectives;
    }

    /**
     * @return the {@link ToolCard} in the game
     */
    public List<ToolCard> getToolCards() {
        return toolCards;
    }

    /**
     * @return the {@link PrivateObjective} of the player
     */
    public PrivateObjective getPrivateObjective() {
        return privateObjective;
    }

    /**
     * Uses the handler to handle this specific setup response
     * @param syncResponseHandler is the object who will handle this response
     */
    @Override
    public void handle(SyncResponseHandler syncResponseHandler){
        syncResponseHandler.handleResponse(this);
    }
}
