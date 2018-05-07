package it.polimi.se2018.Model;


import it.polimi.se2018.Exceptions.NoDieException;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class DraftPool {
    private List<Die> dice; //the 2n + 1 drafted dice are placed here

    public boolean contains(Die die) {return dice.contains(die); }

    public void fillDraftPool(List<Die> draftPool) { //riempie la draft pool con i nuovi 2n+1 dadi presi dalla board
        this.dice = draftPool;
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

    public Die getDie(int index) throws NoDieException{
        if (index>=dice.size()) throw new NoDieException();
        return dice.get(index);
    }

    public List<Die> getAllDice  () {
        return new ArrayList<>(dice);
    }


    public List<Die> modelViewCopy() {
        ArrayList<Die> result = new ArrayList<>();
        for(Die die: dice) {
            result.add(die.modelViewCopy());
        }
        return result;
    }

}

