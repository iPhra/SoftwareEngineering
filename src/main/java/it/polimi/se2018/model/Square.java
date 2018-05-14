package it.polimi.se2018.model;

import it.polimi.se2018.network.messages.Coordinate;

import java.io.Serializable;
import java.util.Objects;

public class Square implements Serializable{
    private final Color color;
    private final int value;
    private Die die;
    private final int row;
    private final int col;

    public Square(Color color, int value, Coordinate coordinate) {
        this.color = color;
        this.value = value;
        this.row=coordinate.getRow();
        this.col=coordinate.getCol();
        die=null;
    }

    public Die getDie() {
        return die;
    }

    public void setDie(Die die) { //if you want to free the square just pass null to this method
        this.die = die;
    }

    public int getCol() {return col; }

    public int getRow() {return row; }

    public boolean isEmpty() {
        return die==null;
    }

    public boolean sameColor(Die die) {
        return color==Color.WHITE || die.getColor().equals(color);
    }

    public boolean sameValue(Die die) {
        return value==0 || die.getValue()==value;
    }

    public Square modelViewCopy() {
        Square result = new Square(color,value,new Coordinate(row,col));
        result.setDie(die == null ? null:die.modelViewCopy());
        return result;
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
}
