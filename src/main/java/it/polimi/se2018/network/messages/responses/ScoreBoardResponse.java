package it.polimi.se2018.network.messages.responses;

import it.polimi.se2018.model.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoardResponse extends Response{
    private final List<String> sortedPlayersNames;
    private final List<Integer> sortedPlayersScores;

    public ScoreBoardResponse(int player, List<Player> sortedPlayers){
        super(player);
        sortedPlayersNames = new ArrayList<>();
        sortedPlayersScores = new ArrayList<>();
        for (Player sortedPlayer : sortedPlayers){
            sortedPlayersNames.add(sortedPlayer.getName());
            sortedPlayersScores.add(sortedPlayer.getScore());
        }
    }

    public List<String> getSortedPlayersNames() {
        return sortedPlayersNames;
    }

    public List<Integer> getSortedPlayersScores() {
        return sortedPlayersScores;
    }

    @Override
    public void handle(ResponseHandler responseHandler){
        responseHandler.handleResponse(this);
    }
}
