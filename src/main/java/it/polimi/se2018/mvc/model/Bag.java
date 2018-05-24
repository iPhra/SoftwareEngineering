package it.polimi.se2018.mvc.model;

import it.polimi.se2018.utils.exceptions.NoDieException;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class represent the bag of the game. It contains the number of die of
 * each (@link Color). It generate new die each round and fill the (@link DraftPool)
 * @author Edoardo Lamonaca
 */
public class Bag {
    /**
     * This list of initeger represent the number of remaining dice for each color
     */
    private final ArrayList<Integer> remainingColors;

    /**
     * This is the number of different color of dice in the game
     */
    private final int colorsNumber;

    /**
     * This is the total number of dice in the bag
     */
    private int diceNumber;

    public Bag(int colorsNumber, int diceNumber) {
        if (diceNumber % colorsNumber != 0) throw new InvalidParameterException("Number of dice and colors is wrong!");
        this.colorsNumber = colorsNumber;
        this.diceNumber = diceNumber;
        remainingColors = new ArrayList<>();
        int coloredDiceNumber = diceNumber / this.colorsNumber;
        for (int i = 0; i < colorsNumber; i++) {
            remainingColors.add(coloredDiceNumber);
        }
    }

    /**
     * Draws a single die from the bag, used by tool cards
     * @return a single (@link Die) create from a color of the Bag
     * @throws NoDieException if there is no die in the bag
     */
    public Die extractDie() throws NoDieException {
        if (diceNumber <= 0) {
            throw new NoDieException();
        }
        int randomDie = new Random().nextInt(diceNumber) + 1;
        int index = 0;
        for (int i = 0; remainingColors.get(i) < randomDie; i++) {
            randomDie -= remainingColors.get(i);
            index += 1;
        }
        remainingColors.set(index, remainingColors.get(index) - 1);
        diceNumber -= 1;
        int randomValueOfDie = new Random().nextInt(6) + 1;
        return new Die(randomValueOfDie, Color.values()[index]);
    }

    /**
     * inserts a single die in the bag, used by tool cards
     * @param die represent the die to put into
     */
    public void insertDie(Die die) {
        diceNumber += 1;
        remainingColors.set(Color.fromColor(die.getColor()), remainingColors.get(Color.fromColor(die.getColor())) + 1);
    }

    /**
     * draws 2n+1 dice putting them in a arraylist, used by Board
     * @param playersNumber number of player in the game. It's used to decide the number of die to extract
     * @return The die created in a List. It's used to fill the (@link DraftPool)
     */
    public List<Die> drawDice(int playersNumber) {
        try {
            ArrayList<Die> drawDice = new ArrayList<>();
            for (int i = 0; i < 2 * playersNumber + 1; i++) {
                drawDice.add(extractDie());
            }
            return drawDice;
        } catch (NoDieException e) {
            return new ArrayList<>();
        }
    }
}
