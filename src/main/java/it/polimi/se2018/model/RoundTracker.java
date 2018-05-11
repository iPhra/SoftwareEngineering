package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class RoundTracker {
    private List<Die>[] dice; //array of arrayList, every position contains an arraylist of dice
    private int turn;

    RoundTracker(int roundsNumber) {
        this.dice = new ArrayList[roundsNumber];
        turn = 0; //this variable matches the actual turns -1
    }

    public Die getDie(int turn, int index) {
        return dice[turn].get(index);
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
        dice[index].add(die);
    }

    public void updateRoundTracker(List<Die> remainingDice) { //increments current turn, fills roundTracker with remaining dice from draftPool
        dice[turn] = remainingDice;
        turn++;
    }

    public int getTurn() {
        return turn;
    }

    public List<Die>[] modelViewCopy() {
        List<Die>[] result = new ArrayList[dice.length];
        for (int i = 0; i < dice.length; i++) {
            result[i]=new ArrayList<>();
            for (Die die : dice[i]) {
                result[i].add((die.modelViewCopy()));
            }
        }
        return result;
    }

}