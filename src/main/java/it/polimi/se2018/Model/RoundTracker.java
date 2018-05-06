package it.polimi.se2018.Model;

import java.util.ArrayList;
import java.util.List;

public class RoundTracker {
    private List<Die>[] dice; //array of arrayList, every position contains an arraylist of dice
    private int turn;

    public RoundTracker(int roundsNumber) {
        this.dice = new ArrayList[roundsNumber];
        turn=0; //this variable matches the actual turns -1
    }

    public boolean contains(Die die) {
        for(List<Die> array : dice) if (array.contains(die)) return true;
        return false;
    }

    public void removeFromRoundTracker(int index, Die die) { //used by ToolCards
        dice[index].remove(die);
    }

    public void addToRoundTracker(int index, Die die) { //used by ToolCards
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

}
