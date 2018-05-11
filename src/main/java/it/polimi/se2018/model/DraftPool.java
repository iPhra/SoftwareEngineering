package it.polimi.se2018.model;


import it.polimi.se2018.utils.exceptions.NoDieException;

import java.util.ArrayList;
import java.util.List;


/**
 * This class represents the Draft Pool containing all available dice in a turn
 * @author Francesco Lorenzo
 */
public class DraftPool {
    /**
     * This List contains 2n+1 dice, where n is the number of players in the game
     */
    private List<Die> dice;

    /**
     * Checks if a given die is present in the Draft Pool
     * @param die represents the die you want to check if it's present
     * @return {@code true} if the Draft Pool contains the given die, {@code false} otherwise
     */
    public boolean contains(Die die) {return dice.contains(die); }

    /**
     * Fills the Draft Pool with a List of dice
     * Used every turn by {@link it.polimi.se2018.controller.Controller} after extracting dice from the Bag
     * @param draftPool is the list to fill the Draft Pool with
     */
    public void fillDraftPool(List<Die> draftPool) { //riempie la draft pool con i nuovi 2n+1 dadi presi dalla board
        this.dice = draftPool;
    }

    /**
     * Adds a single die to the Draft Pool
     * Used by {@link it.polimi.se2018.model.toolcards.ToolCard} and by {@link it.polimi.se2018.controller.Controller} when a player passes
     * @param die is the die to put in the Draft Pool
     */
    public void addToDraftPool(Die die) {
        dice.add(die);
    }

    /**
     * Removes a given die from the Draft Pool
     * Used when you draft by {@link it.polimi.se2018.controller.Controller} and by {@link it.polimi.se2018.model.toolcards.ToolCard}
     * @param die is the die to remove from the Draft Pool
     * @throws NoDieException if the given die isn't contained in the Draft Pool
     */
    public void removeFromDraftPool(Die die) throws NoDieException { //rimuove un dado dalla draft pool dopo averlo inserito nello schema
        if(!contains(die)) throw new NoDieException();
        dice.remove(die);
    }

    /**
     * Returns the die in the position index from the Draft Pool, which is thought as a linear array going from 0 to its current size
     * Used to handle user input by {@link it.polimi.se2018.controller.Controller} and by {@link it.polimi.se2018.model.toolcards.ToolCard}
     * @param index is the index of the die you want to get
     * @return the die itself
     * @throws NoDieException if the index points to an invalid position that doesn't contain a die
     */
    public Die getDie(int index) throws NoDieException{
        if (index>=dice.size() || index<0) throw new NoDieException();
        return dice.get(index);
    }

    /**
     * Returns all the dice currently present in the Draft Pool
     * Used by {@link it.polimi.se2018.controller.Controller} when a turn ends in order to fill the Round Tracker
     * @return a new {@link ArrayList} containing all dice
     */
    public List<Die> getAllDice  () {
        return new ArrayList<>(dice);
    }

    /**
     * Creates a copy of the data structure to be sent to the clients, each die is itself copied
     * Used by {@link it.polimi.se2018.controller.Controller}
     * @return a {@link List} containing all the dice in the Draft Pool
     */
    public List<Die> modelViewCopy() {
        ArrayList<Die> result = new ArrayList<>();
        for(Die die: dice) {
            result.add(die.modelViewCopy());
        }
        return result;
    }

}

