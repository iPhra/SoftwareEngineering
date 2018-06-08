package it.polimi.se2018.network.messages.responses;

import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.network.messages.responses.sync.ScoreBoardResponse;

import java.util.List;

public class EndGameResponse extends Response {
    private ScoreBoardResponse scoreBoardResponse;
    private boolean isPlayerPlaying;

    public EndGameResponse(int playerID) {
        super(playerID);
    }

    public boolean isPlayerPlaying() {
        return isPlayerPlaying;
    }

    public void setPlayerPlaying(boolean playerPlaying) {
        isPlayerPlaying = playerPlaying;
    }

    public ScoreBoardResponse getScoreBoardResponse() {
        return scoreBoardResponse;
    }

    public void setScoreBoardResponse(List<Player> players, boolean lastPlayer) {
        scoreBoardResponse = new ScoreBoardResponse(getPlayer(),players,lastPlayer);
    }

    @Override
    public void handleClass(ResponseHandler responseHandler) {
        responseHandler.handleResponse(this);
    }
}
