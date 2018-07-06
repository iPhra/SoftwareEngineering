package it.polimi.se2018.network.messages.responses.sync;

import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.PrivateObjective;

import java.util.List;

/**
 * This is the class containing the setup of the game, sent by the Server to the Client
 */
public class SetupResponse extends SyncResponse {
    /**
     * These are the {@link Window} a user can choose from
     */
    private final List<Window> windows;

    /**
     * This is the {@link PrivateObjective} belonging to the player this message will be sent to
     */
    private final PrivateObjective privateObjective;

    private final int playersNumber;

    public SetupResponse(int playerID, List<Window> windows, PrivateObjective privateObjective, int playersNumber) {
        super(playerID);
        this.windows = windows;
        this.privateObjective = privateObjective;
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
