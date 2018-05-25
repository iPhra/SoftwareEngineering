package it.polimi.se2018.mvc.model;

import it.polimi.se2018.mvc.controller.Controller;
import it.polimi.se2018.utils.exceptions.DieException;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

/**
 * This class represents a single Die, with its value and color
 * @author Francesco Lorenzo
 */
public class Die implements Serializable {

    /**
     * This is the value of the die
     */
    private int value;

    /**
     * This is the color of the die
     */
    private final Color color;

    public Die(int value, Color color) {
        this.value = value;
        this.color = color;
    }

    /**
     * @return the value of this dice
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of this dice
     * @param value is the value you want to set
     * @throws DieException if the value is greater than 6 or smaller than 1
     */
    public void setValue(int value) throws DieException {
        if (value > 6 || value < 1) {
            throw new DieException();
        }
        this.value = value;
    }

    /**
     * Turns the die on the opposite face
     */
    public void flipDie() {
        value=7-getValue();
    }

    /**
     * Gives a random value to this die
     */
    public void rollDie() {
        this.value = new Random().nextInt(6) + 1;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Used by {@link Controller}
     * @return a copy of this die
     */
    public Die modelViewCopy() {
        return new Die(value, color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, color);
    }

    @Override
    public boolean equals(Object object) {
        return (object instanceof Die) && ((Die) object).getValue() == getValue() && (((Die) object).getColor() == getColor());
    }

    @Override
    public String toString() {
        return value + color.getAbbreviation() + " ";
    }
}

