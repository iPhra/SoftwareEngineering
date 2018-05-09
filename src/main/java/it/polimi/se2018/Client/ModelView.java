package it.polimi.se2018.Client;

import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Model.Square;
import it.polimi.se2018.Network.Messages.Responses.ModelViewResponse;

import java.util.List;

public class ModelView{
    private final List<Die> draftPool; //draft pool
    private final List<Die>[] roundTracker; //ha il riferimento al roundTracker
    private final List<Boolean> usedToolCards; //true if toolcard[i] has already been used
    private final List<Square[][]> maps;
    private final List<Integer> favorPoints;
    private final List<Integer> scores;
    private final List<Die> diceInHand;
    private final int turn; //current turn

    public ModelView (ModelViewResponse modelViewResponse) {
        this.draftPool = modelViewResponse.getDraftPool();
        roundTracker = modelViewResponse.getRoundTracker();
        usedToolCards = modelViewResponse.getUsedToolCards();
        maps = modelViewResponse.getMaps();
        favorPoints = modelViewResponse.getFavorPoints();
        scores = modelViewResponse.getScores();
        diceInHand = modelViewResponse.getDiceInHand();
        turn = modelViewResponse.getTurn();
    }

    public List<Die> getDraftPool() {
        return draftPool;
    }

    public List<Die>[] getRoundTracker() {
        return roundTracker;
    }

    public List<Boolean> getUsedToolCards() {
        return usedToolCards;
    }

    public List<Square[][]> getMaps() {
        return maps;
    }

    public List<Integer> getFavorPoints() {
        return favorPoints;
    }

    public List<Integer> getScores() {
        return scores;
    }

    public List<Die> getDiceInHand() {
        return diceInHand;
    }

    public int getTurn() {
        return turn;
    }

}
