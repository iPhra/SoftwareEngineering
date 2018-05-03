package it.polimi.se2018.Model;

import java.util.Random;

public class Die {

    private int value;
    private final Color color;

    public Die(int value, Color color) {
        this.value = value;
        this.color=color;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    } //used if you already know what value to give

    public void flipDie() {
        setValue(7-getValue());
    } //used by Tool Cards

    public void rollDie() { //gives a random value to the Die
        this.value=new Random().nextInt(6) +1;
    } //gives a random value to the dice

    public Color getColor() {
        return color;
    }

    public Die makeCopy() {
        return new Die(value,color);
    }
}
