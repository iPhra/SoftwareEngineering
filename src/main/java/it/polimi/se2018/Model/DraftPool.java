package it.polimi.se2018.Model;

import it.polimi.se2018.Exceptions.DieNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class DraftPool {
    private ArrayList<Die> dice; //the 2n + 1 drafted dice are placed here

    public List<Die> getDraftPool() {return (ArrayList<Die>) dice.clone(); }

    public boolean contains(Die die) {return dice.contains(die); }

    void fillDraftPool(ArrayList<Die> draftPool) { //riempie la draft pool con i nuovi 2n+1 dadi presi dalla board
        this.dice = draftPool;
    }

    public void addToDraftPool(Die die) {
        dice.add(die);
    }

    public void removeFromDraftPool(Die die) { //rimuove un dado dalla draft pool dopo averlo inserito nello schema
        dice.remove(die);
    }

    void emptyDraftPool () { //svuota la draftPool
        dice.clear();
    }

}

