package it.polimi.se2018.Model;

import java.util.ArrayList;

public class RoundTracker {
    private ArrayList<Die>[] dice; //array of arrayList, every position contains an arraylist of dice
    private int turn;

    public RoundTracker(int roundsNumber) {
        this.dice = new ArrayList[roundsNumber];
        turn=0; //this variable matches the actual turns -1
    }

    public boolean contains(Die die) {
        for(int i=0; i<dice.length;i++) if (dice[i].contains(die)) return true;
        return false;
    }

    public void removeFromRoundTracker(int index, Die die) { //used by ToolCards
        dice[index].remove(die);
    }

    public void addToRoundTracker(int index, Die die) { //used by ToolCards
        dice[index].add(die);
    }

    void updateRoundTracker(ArrayList<Die> remainingDice) { //increments current turn, fills roundTracker with remaining dice from draftPool
        dice[turn]=remainingDice;
        turn ++;
    }

    public int getTurn() {return turn;}

    public ArrayList<Die>[] modelViewCopy() {
        ArrayList<Die>[] result = new ArrayList[dice.length];
        ArrayList<Die> array = new ArrayList<>();
        for(ArrayList<Die> arrayList: dice) {
            for(Die die: arrayList) {
                array.add(die.modelViewCopy());
            }
            array.clear();
        }
        return result;
    }

}
