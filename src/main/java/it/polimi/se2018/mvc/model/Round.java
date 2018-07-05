package it.polimi.se2018.mvc.model;

import it.polimi.se2018.mvc.controller.Controller;
import it.polimi.se2018.mvc.controller.ToolCardController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This class represents a single Round, with the playersIDs in the order they have to play in the round
 */
public class Round {
    /**
     * This is the arraylist of player's IDs, in order. example: ABCDDCBA
     */
    private final List<Integer> playersOrder;

    private final int playersNumber;

    /**
     * This is the number of unique players who still haven't played in the round. it's 0 when you are in the middle of the array (every played played once)
     */
    private int missingPlayers;

    /**
     * This is the ID of the current player
     */
    private int currentPlayer;

    /**
     * This is the index of the array playersOrder
     */
    private int currentPlayerIndex;

    /**
     * This is the number of this round, it goes from 1 to 10
     */
    private final int roundNumber;

    public Round(List<Integer> playersIDs, int roundNumber) {
        playersOrder = new ArrayList<>(playersIDs);
        Collections.reverse(playersIDs);
        playersOrder.addAll(playersIDs);
        this.roundNumber = roundNumber;
        currentPlayerIndex=0;
        currentPlayer=playersOrder.get(currentPlayerIndex);
        missingPlayers=playersOrder.size()/2;
        playersNumber = playersOrder.size()/2;
    }

    /**
     * This method changes the current player that must perform a turn
     */
    private void changePlayer() {
        currentPlayerIndex++;
        currentPlayer=playersOrder.get(currentPlayerIndex);
    }

    /**
     * @return the array of playersIDs representing the order of the players
     */
    public List<Integer> getPlayersOrder() {
        return playersOrder;
    }

    /**
     * @return the index of the array playersOrder
     */
    public int getCurrentPlayerID() {return playersOrder.get(currentPlayerIndex);}

    /**
     * @return the number of this round
     */
    public int getRoundNumber () {
        return roundNumber;
    }

    /**
     * Used by {@link Controller} to check if it's the turn of a player that wants to perform a move
     * @param player is the player whom you want to know if it's his turn
     * @return true if it's player's turn, false otherwise
     */
    public boolean isYourTurn(Player player) {
        return player.getId()==currentPlayer;
    }

    /**
     * Used by {@link ToolCardController} because RunningPliers can be used only by a player
     * that is performing his second turn of the round
     * @return true if we are in the first rotation of the round, false otherwise
     */
    public boolean isFirstRotation() {
        return missingPlayers>0;
    }

    /**
     * @return true if it's the last turn of the round, false otherwise
     */
    public boolean isLastTurn () {
        return currentPlayerIndex == playersOrder.size() - 1;
    }

    /**
     * This method is called when a player ended his turn and you need to change player with the following player
     */
    public void changeTurn() {
        changePlayer();
        missingPlayers--; //when i use the toolcards i check if it's <= 0
    }

    /**
     * This method builds the next array playersOrder and gives it to
     * the constructor of the next round with the new roundNumber
     * Used by {@link Controller} to start a new round
     * @return the next Round
     */
    public Round changeRound() {
        List<Integer> newPlayersOrder = new ArrayList<>();
        for(int i = 0; i < playersNumber - 1; i++){
            newPlayersOrder.add(playersOrder.get(i+1));
        }    //given ABCDDCBA, now we have BCD
        newPlayersOrder.add(playersOrder.get(0)); //BCDA
        return new Round(newPlayersOrder,roundNumber + 1);
    }

    /**
     * Used by {@link ToolCardController} because RunningPliers toolCard needs it to allow
     * the player to place the die twice and skip your second turn
     * this method, specifically, let the player skip his second turn (deleting the player from the second half of the array)
     * note that the toolcard must set hasPlacedDie and hasUsedCard
     * requires being in the first half of the array. if not, tool cards throw an exception
     */
    public void denyNextTurn() {
        for (int i = 0; i < playersOrder.size(); i++) {
            if (playersOrder.get(i) == currentPlayer && i != currentPlayerIndex) {
                playersOrder.remove(i);
            }
        }
    }

    /**
     *
     * @param o it's the object that user wants to know if it's equal to this Round
     * @return true if o it's equals to this, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Round round = (Round) o;
        return missingPlayers == round.missingPlayers &&
                currentPlayer == round.currentPlayer &&
                currentPlayerIndex == round.currentPlayerIndex &&
                roundNumber == round.roundNumber &&
                Objects.equals(playersOrder, round.playersOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playersOrder, missingPlayers, currentPlayer, currentPlayerIndex, roundNumber);
    }
}
