package it.polimi.se2018.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ModelView implements Serializable{
    private final List<Die> draftPool; //draft pool
    private final List<Die>[] roundTracker; //ha il riferimento al roundTracker
    private final List<Boolean> usedToolCards; //true if toolcards[i] has already been used
    private final List<Square[][]> maps;
    private final List<Integer> favorPoints;
    private final List<Integer> scores;
    private final List<Die> diceInHand;
    private final int turn; //current turn

    public ModelView(Board board) {
        usedToolCards = new ArrayList<>();
        maps = new ArrayList<>();
        favorPoints = new ArrayList<>();
        scores = new ArrayList<>();
        diceInHand = new ArrayList<>();
        for (boolean b : board.getToolCardsUsage()) {
            usedToolCards.add(b);
        }
        for (Player player : board.getPlayers()) {
            maps.add(player.getMap().modelViewCopy());
            favorPoints.add(player.getFavorPoints());
            scores.add(player.getScore());
            diceInHand.add(player.getDieInHand());
        }
        turn = board.getRoundTracker().getTurn();
        draftPool = board.getDraftPool().modelViewCopy();
        roundTracker = board.getRoundTracker().modelViewCopy();
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
