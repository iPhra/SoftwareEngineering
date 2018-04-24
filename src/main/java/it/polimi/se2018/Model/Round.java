package it.polimi.se2018.Model;

import java.util.ArrayList;

public class Round {
    private ArrayList<Integer> playersOrder; //arraylist of player's IDs. example: ABCDDCBA
    private int missingPlayers; //number of unique players who still haven't played in the round. it's 0 when you are in the middle of the array (every played played once)
    private int currentPlayer; //id of the player
    private int currentPlayerIndex; //index of the array
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
        currentPlayerIndex++;
        currentPlayer=playersOrder.get(currentPlayerIndex);
    }

    //called when a player ended his turn and you need to change player with the following player
    public void changeTurn() {
        changePlayer();
        missingPlayers--; //when i use the toolcard i check if it's <= 0
        hasPlacedDie = false;
        hasUsedCard = false;

    }

    Round endRound() {
        return null; //placeholder
    }

    // used by the toolcard that allows you to place the die twice and skip your second turn
    //this method, specifically, let the player skip his second turn (deleting the player from the second half of the array)
    //toolcard MUST set hasPlacedDie and hasUsedCard
    //requires being in the first half of the array. if not, toolcard throws an exception
    public void denyNextTurn() {
        playersOrder.remove(playersOrder.size()- currentPlayerIndex -1);
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
