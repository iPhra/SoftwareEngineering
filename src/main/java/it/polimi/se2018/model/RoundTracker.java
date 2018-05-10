package it.polimi.se2018.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class RoundTracker implements Iterable<Die>{
    private List<Die>[] dice; //array of arrayList, every position contains an arraylist of dice
    private int turn;

    RoundTracker(int roundsNumber) {
        this.dice = new ArrayList[roundsNumber];
        turn=0; //this variable matches the actual turns -1
    }

    public Die getDie (int turn, int index) {
        return dice[turn].get(index);
    }

    public boolean contains(Die die) {
        for(List<Die> array : dice) if (array.contains(die)) return true;
        return false;
    }

    public void removeFromRoundTracker(Die die) {
        for(List<Die> currentList : dice) {
            for (Die currentDie : currentList) {
                if (currentDie.equals(die)) currentList.remove(die);
            }
        }
    }

    public void addToRoundTracker(int index, Die die) { //used by toolcards
        dice[index].add(die);
    }

    public void updateRoundTracker(List<Die> remainingDice) { //increments current turn, fills roundTracker with remaining dice from draftPool
        dice[turn]= remainingDice;
        turn ++;
    }

    public int getTurn() {return turn;}

    public List<Die>[] modelViewCopy() {
        List<Die>[] result = new ArrayList[dice.length];
        for(int i=0; i<result.length; i++) {
            for(Die die: dice[i]) {
                result[i].add((die.modelViewCopy()));
            }
        }
        return result;
    }

    public Iterator<Die> iterator(){
        return new DieIterator();
    }

    private class DieIterator implements Iterator<Die> {
        List<Die> currentTurn;
        int currentTurnIndex;
        int currentDieIndex;

        DieIterator(){
            currentTurnIndex = 0;
            currentDieIndex = 0;
            currentTurn=dice[0];
        }

        public boolean hasNext() {
            return !(currentTurnIndex==dice.length && currentDieIndex==currentTurn.size()-1);
        }

        public Die next() {
            if (!hasNext()) throw new NoSuchElementException("No more elements available in the iterator");
            Die result = currentTurn.get(currentDieIndex);
            if(currentDieIndex<currentTurn.size()-1) currentDieIndex++;
            else {
                currentDieIndex=0;
                currentTurnIndex++;
                currentTurn=dice[currentTurnIndex];
            }
            return result;
        }
    }

}
