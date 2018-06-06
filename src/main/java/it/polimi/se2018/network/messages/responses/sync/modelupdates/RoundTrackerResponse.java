package it.polimi.se2018.network.messages.responses.sync.modelupdates;

import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.network.messages.responses.sync.SyncResponse;
import it.polimi.se2018.network.messages.responses.sync.SyncResponseHandler;

import java.util.List;

public class RoundTrackerResponse extends ModelUpdateResponse{
    private final List<List<Die>> roundTracker;

    public RoundTrackerResponse(int playerID, Board board) {
        super(playerID,board.getStateID(), board);
        this.roundTracker = board.getRoundTracker().modelViewCopy();
    }

    public List<List<Die>> getRoundTracker() {
        return roundTracker;
    }

    /**
     * Uses the handler to handle this specific response
     * Implemented by each specific SyncResponse subclass
     *
     * @param syncResponseHandler is the object who will handle this response
     */
    @Override
    public void handle(SyncResponseHandler syncResponseHandler) {
        syncResponseHandler.handleResponse(this);
    }
}
