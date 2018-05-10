package it.polimi.se2018.model;

import it.polimi.se2018.network.messages.Coordinate;

public class Square {
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
        return die.getValue()==value;
    }

    public Square modelViewCopy() {
        Square result = new Square(color,value,new Coordinate(row,col));
        result.setDie(die.modelViewCopy());
        return result;
    }

}
