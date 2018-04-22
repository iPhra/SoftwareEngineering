package it.polimi.se2018.Model;

import java.util.ArrayList;

public class RoundTracker {
    private ArrayList<Die>[] roundTracker; //array of arrayList, every position contains an arraylist of dice
    private int turn;

    public RoundTracker() {
        this.roundTracker = new ArrayList[10];
        turn=0;
    }

    public ArrayList<Die>[] getRoundTracker() {
        return roundTracker;
    }

    void removeFromRoundTracker(int index, Die die) { //removes a die from round tracker
    }

    void addToRoundTracker(int index, Die die) { //adds a die to the round tracker
    }

    void updateRoundTracker(ArrayList<Die> remainingDice) { //increments turn, fills roundTracker with remaining dice from draft Pool
    }

}
