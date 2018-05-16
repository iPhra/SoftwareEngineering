package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RoundTracker {
    private List<List<Die>> dice; //array of arrayList, every position contains an arraylist of dice
    private int turn;

    RoundTracker(int roundsNumber) {
        dice = new ArrayList<>();
        for(int i=0; i<roundsNumber; i++) {
            dice.add(new ArrayList<>());
        }
        turn = 0; //this variable matches the actual turns -1
    }

    public Die getDie(int turn, int index) {
        return dice.get(turn).get(index);
    }

    public boolean contains(Die die) {
        for (List<Die> array : dice) if (array.contains(die)) return true;
        return false;
    }

    public void removeFromRoundTracker(Die die) {
        for (List<Die> currentList : dice) {
            for (int j = 0; j < currentList.size(); j++)
                if (currentList.get(j).equals(die)) currentList.remove(die);
        }
    }

    public void addToRoundTracker(int index, Die die) { //used by toolcards
        dice.get(index).add(die);
    }

    public void updateRoundTracker(List<Die> remainingDice) { //increments current turn, fills roundTracker with remaining dice from draftPool
        dice.set(this.turn,  remainingDice);
        this.turn++;
    }

    public int getTurn() {
        return turn;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoundTracker that = (RoundTracker) o;
        return turn == that.turn &&
                Objects.equals(dice, that.dice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dice, turn);
    }
}