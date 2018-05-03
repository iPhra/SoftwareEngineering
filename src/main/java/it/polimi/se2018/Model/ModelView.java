package it.polimi.se2018.Model;

import java.util.ArrayList;
import java.util.List;

//updates from model to view
public class ModelView {
    private final ArrayList<Die> draftPool; //draft pool
    private final ArrayList<Die> roundTracker; //ha il riferimento al roundTracker
    private final ArrayList<Boolean> usedToolCards; //true if toolcard[i] has already been used
    private final ArrayList<Square[][]> maps;
    private final ArrayList<Integer> favorPoints;
    private final ArrayList<Integer> scores;
    private final ArrayList<Die> diceInHand;
    private final int turn; //current turn

    public ModelView(List<Die> draftPool, List<Die> roundTracker, List<Boolean> usedToolCards, List<Square[][]> maps, List<Integer> favorPoints, List<Integer> scores, List<Die> diceInHand, int turn) {
        this.maps = (ArrayList<Square[][]>)maps;
        this.favorPoints = (ArrayList<Integer>)favorPoints;
        this.scores = (ArrayList<Integer>)scores;
        this.diceInHand = (ArrayList<Die>)diceInHand;
        this.draftPool = (ArrayList<Die>)draftPool;
        this.roundTracker = (ArrayList<Die>)roundTracker;
        this.usedToolCards = (ArrayList<Boolean>)usedToolCards;
        this.turn = turn;
    }

    public ArrayList<Die> getDraftPool() {
        return draftPool;
    }

    public ArrayList<Die> getRoundTracker() {
        return roundTracker;
    }

    public ArrayList<Boolean> getUsedToolCards() {
        return usedToolCards;
    }

    public ArrayList<Square[][]> getMaps() {
        return maps;
    }

    public ArrayList<Integer> getFavorPoints() {
        return favorPoints;
    }

    public ArrayList<Integer> getScores() {
        return scores;
    }

    public ArrayList<Die> getDiceInHand() {
        return diceInHand;
    }

    public int getTurn() {
        return turn;
    }
}
