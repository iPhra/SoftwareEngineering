package it.polimi.se2018.Model;

import java.util.ArrayList;

public class Round {
    private ArrayList<Integer> playersOrder; //arraylist of player's IDs
    private int missingPlayers; //number of unique players who still haven't played in the round
    private int currentPlayer;
    private int currentPlayerIndex;
    private boolean hasPlacedDie;
    private boolean hasUsedCard;

    public Round(ArrayList<Integer> playersOrder) {
        this.playersOrder = playersOrder;
        currentPlayerIndex=0;
        currentPlayer=playersOrder.get(currentPlayerIndex);
        missingPlayers=playersOrder.size()/2;
        hasPlacedDie = false;
        hasUsedCard = false;
    }

    public boolean isYourTurn(Player player) {
        return player.getId()==currentPlayer;
    }

    public boolean isFirstTurn() {
        return missingPlayers>0;
    }

    private void changePlayer() {
        currentPlayer=playersOrder.get(currentPlayerIndex+1);
        currentPlayerIndex++;
    }

    public void changeTurn() {
    }

    Round endRound() {
        return null; //placeholder
    }

    public void denyNextTurn() {
    }

    public boolean hasPlacedDie() {
        return hasPlacedDie;
    }

    public boolean hasUsedCard() {
        return hasUsedCard;
    }

    public void setHasPlacedDie(boolean hasPlacedDie) {
        this.hasPlacedDie = hasPlacedDie;
    }

    public void setHasUsedCard(boolean hasUsedCard) {
        this.hasUsedCard = hasUsedCard;
    }
}
