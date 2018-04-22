package it.polimi.se2018.Model;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public class Bag {
    private final ArrayList<Integer> remainingColors; //ogni elemento dell'arraylist corrisponde a un colore e indica quanti dati sono rimasti nella bag di quel colore
    private final int colorsNumber; //numero totale di colori che ci sono. (nel gioco è 5, facciamo così per renderlo estendibile)
    private final int diceNumber; //numero totale di dadi che ci sono. (nel gioco è 90, facciamo così per renderlo estendibile)
    private final int coloredDiceNumber; //numero di dati di uno specifico colore

    //gli passi il numero totale di colori e il numero totale di dadi e inizializza remainingColors.
    //non va bene che non siano divisibili, quindi se succede lancia un'eccezione.
    public Bag(int colorsNumber, int diceNumber) {
        if (diceNumber%colorsNumber !=0) throw new InvalidParameterException("Number of dice and colors is wrong!");
        else {
            this.colorsNumber = colorsNumber;
            this.diceNumber = diceNumber;
            remainingColors = new ArrayList(colorsNumber);
            coloredDiceNumber = diceNumber/colorsNumber;
            for (int i = 0; i<coloredDiceNumber; i++) {
                remainingColors.set(i,new Integer(coloredDiceNumber));
            }
        }
    }

    //draws a single die from the bag, used by tool cards
    Die extractDie () {
        return null;
    }

    void insertDie (Die die) { //inserts a single die in the bag, used by tool cards
    }

    //draws 2n+1 dice putting them in a arraylist, used by Board
    ArrayList<Die> drawDice (int playersNumber) {
        return null; //se non avessimo scritto questo return, ci avrebbe dato errore e non ci avrebbe lasciato scrivere il prototipo del metodo
    }



}
