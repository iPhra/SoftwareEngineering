package it.polimi.se2018.Model;

import it.polimi.se2018.Exceptions.DieException;

import java.util.Random;

public class Die {

    private int value;
    private final Color color;

    public Die(int value, Color color) {
        this.value = value;
        this.color = color;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) throws DieException {
        if (value > 6 || value < 1) {
            throw new DieException();
        }
        this.value = value;
    } //used if you already know what value to give

    public void flipDie() throws DieException {
        setValue(7 - getValue());
    } //used by Tool Cards

    public void rollDie() { //gives a random value to the Die
        this.value = new Random().nextInt(6) + 1;
    } //gives a random value to the dice

    public Color getColor() {
        return color;
    }

    public Die modelViewCopy() {
        return new Die(value, color);
    }

    @Override
    public boolean equals(Object object) {
        return (object instanceof Die) && ((Die) object).getValue() == getValue() && ((Die) object).getColor() == getColor();
    }

}

