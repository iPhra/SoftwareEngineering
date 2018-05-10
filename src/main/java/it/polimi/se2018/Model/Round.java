package it.polimi.se2018.Model;

import java.util.ArrayList;
import java.util.List;

public class Round {
    private List<Integer> playersOrder; //arraylist of player's IDs. example: ABCDDCBA
    private int missingPlayers; //number of unique players who still haven't played in the round. it's 0 when you are in the middle of the array (every played played once)
    private int currentPlayer; //id of the player
    private int currentPlayerIndex; //index of the array
    private final int roundNumber; //number of this round, goes from 1 to 10

    public Round(List<Integer> playersOrder, int roundNumber) {
        this.playersOrder = playersOrder;
        this.roundNumber = roundNumber;
        currentPlayerIndex=0;
        currentPlayer=playersOrder.get(currentPlayerIndex);
        missingPlayers=playersOrder.size()/2;
    }

    public int getCurrentPlayerIndex() {return currentPlayerIndex;}

    public int getRoundNumber () {
        return roundNumber;
    }

    public boolean isYourTurn(Player player) {
        return player.getId()==currentPlayer;
    }

    public boolean isFirstRotation() {
        return missingPlayers>0;
    }

    public boolean isLastTurn () {
        return currentPlayerIndex == playersOrder.size() - 1;
    }

    private void changePlayer() {
        currentPlayerIndex++;
        currentPlayer=playersOrder.get(currentPlayerIndex);
    }

    //called when a player ended his turn and you need to change player with the following player
    public void changeTurn() {
        changePlayer();
        missingPlayers--; //when i use the toolcard i check if it's <= 0
    }
    //changeRound() build the next array player order
    //and give it to the costructor of the next round with the new roundNumber
    public Round changeRound() {
        List<Integer> newPlayersOrder = new ArrayList<>();
        for(int i=0; i < playersOrder.size()/2 - 1; i++){
            newPlayersOrder.add(playersOrder.get(i+1));
        }    //given ABCDDCBA, now we have BCD
        newPlayersOrder.add(playersOrder.get(0)); //BCDA
        newPlayersOrder.add(playersOrder.get(0)); //BCDAA
        for(int i=playersOrder.size()/2; i<playersOrder.size() - 1; i++ ){
            newPlayersOrder.add(playersOrder.get(i));
        } //BCDAADCB, that's what we wanted
        return new Round(newPlayersOrder,roundNumber + 1);
    }

    // used by the toolcard that allows you to place the die twice and skip your second turn
    //this method, specifically, let the player skip his second turn (deleting the player from the second half of the array)
    //toolcard MUST set hasPlacedDie and hasUsedCard
    //requires being in the first half of the array. if not, toolcard throws an exception
    public void denyNextTurn() {
        playersOrder.remove(playersOrder.size()- currentPlayerIndex -1);
    }

}
