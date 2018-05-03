package it.polimi.se2018.Model;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bag {
    private final ArrayList<Integer> remainingColors; //ogni elemento dell'arraylist corrisponde a un colore e indica quanti dati sono rimasti nella bag di quel colore
    private final int colorsNumber; //numero totale di colori che ci sono. (nel gioco è 5, facciamo così per renderlo estendibile)
    private int diceNumber; //numero totale di dadi che ci sono. (nel gioco è 90, facciamo così per renderlo estendibile)
    private final int coloredDiceNumber; //numero di dadi di uno specifico colore

    //gli passi il numero totale di colori e il numero totale di dadi e inizializza remainingColors.
    //non va bene che non siano divisibili, quindi se succede lancia un'eccezione.
    public Bag(int colorsNumber, int diceNumber) {
        if (diceNumber%colorsNumber !=0) throw new InvalidParameterException("Number of dice and colors is wrong!");
        this.colorsNumber = colorsNumber;
        this.diceNumber = diceNumber;
        remainingColors = new ArrayList<>(this.colorsNumber);
        coloredDiceNumber = diceNumber/this.colorsNumber;
        for (int i = 0; i<coloredDiceNumber; i++) {
            remainingColors.set(i,coloredDiceNumber);
        }
}

    //draws a single die from the bag, used by tool cards
    public Die extractDie () {
        int randomDie = new Random().nextInt(diceNumber) + 1;
        int index = 0;
        for (int i = 0; remainingColors.get(i) < randomDie; i++) {
            randomDie -= remainingColors.get(i);
            index += 1;
        }
        remainingColors.set(index, remainingColors.get(index) - 1);
        diceNumber -= 1;
        int randomValueOfDie = new Random().nextInt(6) + 1;
        return new Die (randomValueOfDie, Color.fromInt(index));
    }

    public void insertDie (Die die) { //inserts a single die in the bag, used by tool cards
        diceNumber += 1;
        remainingColors.set(Color.fromColor(die.getColor()), remainingColors.get(Color.fromColor(die.getColor())) + 1);
    }

    //draws 2n+1 dice putting them in a arraylist, used by Board
    public List<Die> drawDice (int playersNumber) {
        ArrayList<Die> drawDice = new ArrayList<>();
        for (int i = 0; i < 2 * playersNumber + 1; i++) {
            drawDice.add(extractDie());
        }
        return drawDice;
    }

}
