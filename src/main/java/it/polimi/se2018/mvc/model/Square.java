package it.polimi.se2018.mvc.model;

import it.polimi.se2018.network.messages.Coordinate;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represent a single Square. It's a part of a single (@link Window).
 * It could be empty or cointains a (@link Die)
 * @author Edoardo Lamonaca
 */
public class Square implements Serializable{
    /**
     * This is the possible limitation of color of the Square.
     * No Die of this color could be put by a normal action in this Square
     */
    private final Color color;

    /**
     * This is the possible limitation of value of the Square.
     * No Die with this value could be put by a normal action in this Square
     */
    private final int value;

    /**
     * This is the die in the Square. It could be null. At the beginning it must be null
     */
    private Die die;

    /**
     * This is the x coordinate in the Window of this Square
     */
    private final int row;

    /**
     * This is the y coordinate in the Window of this Square
     */
    private final int col;

    private final String constraintPath;

    public Square(Color color, int value, Coordinate coordinate, String constraintPath) {
        this.color = color;
        this.value = value;
        this.row=coordinate.getRow();
        this.col=coordinate.getCol();
        this.constraintPath = constraintPath;
        die=null;
    }

    /**
     * @return the limitation of color of this Square
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return the limitation of value of this Square
     */
    public int getValue() {
        return value;
    }

    public Die getDie() {
        return die;
    }

    public void setDie(Die die) { //if you want to free the square just pass null to this method
        this.die = die;
    }

    public int getCol() {return col; }

    public int getRow() {return row; }

    public String getConstraintPath() {
        return constraintPath;
    }

    public String getDescription() {
        return ("row " + row + " col " + col);
    }
    /**
     * @return a boolean that show if there are a die in this Square
     */
    public boolean isEmpty() {
        return die==null;
    }

    /**
     * @param die the die that you would put in this Square
     * @return a boolean if the color of the die is the same of the Square or
     * the Square don't have limitation of Color
     */
    public boolean sameColor(Die die) {
        return color==Color.WHITE || die.getColor().equals(color);
    }

    /**
     * @param die the die that you would put in this Square
     * @return a boolean if the value of the die is the same of the Square or
     * the Square don't have limitation of value
     */
    public boolean sameValue(Die die) {
        return value==0 || die.getValue()==value;
    }

    public Square modelViewCopy() {
        Square result = new Square(color,value,new Coordinate(row,col),constraintPath);
        result.setDie(die == null ? null:die.modelViewCopy());
        return result;
    }

    public Die popDie () {
        Die dieToGet = die;
        die = null;
        return dieToGet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Square square = (Square) o;
        return value == square.value &&
                row == square.getRow() &&
                col == square.getCol() &&
                color == square.color &&
                Objects.equals(die, square.getDie());
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, value, die, row, col);
    }

    @Override
    public String toString() {
        String result;
        if (!isEmpty()) {
            return die.toString();
        } else {
            if (!color.equals(Color.WHITE))
                result = "-" + color.getAbbreviation() + " ";
            else if (value != 0)
                result = "-" + value  + " ";
            else
                result = "-- ";
        }
        return result;
    }
}
