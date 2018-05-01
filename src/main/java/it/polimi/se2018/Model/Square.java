package it.polimi.se2018.Model;

public class Square {
    private final Color color;
    private final int value;
    private Die die;
    private final int row;
    private final int col;
    private final Map map;

    public Square(Color color, int value, int row, int col, Map map) {
        this.color = color;
        this.value = value;
        this.row=row;
        this.col=col;
        this.map=map;
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



}
