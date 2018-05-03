package it.polimi.se2018.Model;


import java.util.ArrayList;
import java.util.List;

public class DraftPool {
    private ArrayList<Die> dice; //the 2n + 1 drafted dice are placed here

    public boolean contains(Die die) {return dice.contains(die); }

    public void fillDraftPool(List<Die> draftPool) { //riempie la draft pool con i nuovi 2n+1 dadi presi dalla board
        this.dice = (ArrayList<Die>)draftPool;
    }

    public void addToDraftPool(Die die) {
        dice.add(die);
    }

    public void removeFromDraftPool(Die die) { //rimuove un dado dalla draft pool dopo averlo inserito nello schema
        dice.remove(die);
    }

    public void emptyDraftPool () { //svuota la draftPool
        dice.clear();
    }

    public List<Die> modelViewCopy() {
        ArrayList<Die> result = new ArrayList<>();
        for(Die die: dice) {
            result.add(die.modelViewCopy());
        }
        return result;
    }

}

