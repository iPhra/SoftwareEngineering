package it.polimi.se2018.mvc.model;

import it.polimi.se2018.mvc.controller.ModelView;
import it.polimi.se2018.mvc.controller.ToolCardController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents the RoundTracker of a single game
 */
public class RoundTracker {

    /**
     * This represents every die in the roundtracker. every position represents a round
     * and contains an list of dice, that are the dice put in the roundtracker in that round
     */
    private final List<List<Die>> dice;

    /**
     * This is current round
     */
    private int round;

    public RoundTracker(int roundsNumber) {
        dice = new ArrayList<>();
        for(int i=0; i<roundsNumber; i++) {
            dice.add(new ArrayList<>());
        }
        round = 0; //this variable matches the actual turns -1
    }

    /**
     *
     * @param round it's the round user wants a die from
     * @param index it's the index that specifies exactly what die user wants from that round
     * @return the die on the roundtracker specified by round and index
     */
    public Die getDie(int round, int index) {
        return dice.get(round).get(index);
    }

    /**
     *
     * @return the value of the current round
     */
    public int getRound() {
        return round;
    }

    /**
     * @param die it's the die user wants to check if it's contained in the roundtracker
     * @return true if die is in the roundtracker, false otherwise
     */
    public boolean contains(Die die) {
        for (List<Die> array : dice) if (array.contains(die)) return true;
        return false;
    }

    /**
     * Removes a given die from the roundtracker
     * @param die it's the die tha has to be removed
     */
    public void removeFromRoundTracker(Die die) {
        for (List<Die> currentList : dice) {
            for (int j = 0; j < currentList.size(); j++)
                if (currentList.get(j).equals(die)) currentList.remove(die);
        }
    }

    /**
     * Adds a given die to the roundtracker, in a given index (that is a round)
     * Used by {@link ToolCardController} because LensCutter toolCard needs it
     * @param index it's the index user wants to add the die to
     * @param die it's the die user wants to add to the roundtracker
     */
    public void addToRoundTracker(int index, Die die) {
        dice.get(index).add(die);
    }

    /**
     * Increments current round, fills roundTracker with remaining dice from draftPool. It's called when the round ends
     * @param remainingDice the remaining dice in the draftPool that must fill the roundtracker
     */
    public void updateRoundTracker(List<Die> remainingDice) {
        dice.set(this.round,  remainingDice);
        this.round++;
    }

    /**
     * Checks if the round tracker is void (if there is no die)
     * @return true if there is no die in the round tracker, false otherwise
     */
    public boolean isVoid(){
        for (List<Die> diceList : dice){
            if (!diceList.isEmpty()) return false;
        }
        return true;
    }

    /**
     * Used by {@link ModelView}, in the constructor, when it's needed to copy the whole Board
     * to send it to a client
     * @return a copy of attribute dice
     */
    public List<List<Die>> modelViewCopy() {
        List<List<Die>> result = new ArrayList<>();
        for (int i = 0; i < dice.size(); i++) {
            result.add(new ArrayList<>());
            for (Die die : dice.get(i)) {
                result.get(i).add((die.modelViewCopy()));
            }
        }
        return result;
    }

    /**
     * @param o it's the object that user wants to know if it's equal to this RoundTracker
     * @return true if o it's equals to this, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoundTracker that = (RoundTracker) o;
        return round == that.round &&
                Objects.equals(dice, that.dice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dice, round);
    }
}