package it.polimi.se2018.Model;

import project_temp.Exceptions.DieNotFoundException;

import java.util.ArrayList;

public class DraftPool {
    private ArrayList<Die> draftPool; //i 2n+1 dadi draftati vanno messi qui

    public ArrayList<Die> getDraftPool() {
        return draftPool;
    }

    void fillDraftPool(ArrayList<Die> draftPool) { //riempie la draft pool con i nuovi 2n+1 dadi presi dalla board
        this.draftPool = draftPool;
    }

    void addToDraftPool(Die die) {
        draftPool.add(die);
    }

    void removeFromDraftPool(Die die) throws DieNotFoundException { //rimuove un dado dalla draft pool dopo averlo inserito nello schema
        if (!draftPool.contains(die)) throw new DieNotFoundException("No such die found in Draft Pool to remove");
        draftPool.remove(die);
    }

    void emptyDraftPool () { //svuota la draftPool
        draftPool.clear();
    }

}

