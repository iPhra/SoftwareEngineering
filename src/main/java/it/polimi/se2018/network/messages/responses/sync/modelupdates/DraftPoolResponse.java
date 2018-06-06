package it.polimi.se2018.network.messages.responses.sync.modelupdates;

import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.network.messages.responses.sync.SyncResponse;
import it.polimi.se2018.network.messages.responses.sync.SyncResponseHandler;

import java.util.List;

public class DraftPoolResponse extends ModelUpdateResponse {
    private final List<Die> draftPool;

    public DraftPoolResponse(int playerID, Board board) {
        super(playerID,board.getStateID(),board);
        draftPool = board.getDraftPool().modelViewCopy();
    }

    public List<Die> getDraftPool() {
        return draftPool;
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
