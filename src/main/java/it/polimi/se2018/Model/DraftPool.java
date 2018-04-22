package it.polimi.se2018.Model;

import it.polimi.se2018.Exceptions.DieNotFoundException;

import java.util.ArrayList;

public class DraftPool {
    private ArrayList<Die> dice; //i 2n+1 dadi draftati vanno messi qui

    public ArrayList<Die> getDraftPool() {
        return dice;
    }

    void fillDraftPool(ArrayList<Die> draftPool) { //riempie la draft pool con i nuovi 2n+1 dadi presi dalla board
        this.dice = draftPool;
    }

    void addToDraftPool(Die die) {
        dice.add(die);
    }

    void removeFromDraftPool(Die die) throws DieNotFoundException { //rimuove un dado dalla draft pool dopo averlo inserito nello schema
        if (!dice.contains(die)) throw new DieNotFoundException("No such die found in Draft Pool to remove");
        dice.remove(die);
    }

    void emptyDraftPool () { //svuota la draftPool
        dice.clear();
    }

}

