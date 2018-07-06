package it.polimi.se2018.network.messages.responses.sync.modelupdates;

import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.network.messages.responses.sync.SyncResponseHandler;

public class WindowResponse extends ModelUpdateResponse {
    private final Square[][] window;

    public WindowResponse(int playerID, Board board, int playerWindowID) {
        super(playerID,board.getStateID(),board);
        window = board.getPlayerByID(playerWindowID).getWindow().modelViewCopy();
    }

    public Square[][] getWindow() {
        return window;
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
