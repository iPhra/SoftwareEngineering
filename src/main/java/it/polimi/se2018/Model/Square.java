package it.polimi.se2018.Model;

public class Square {
    private final Color color;
    private final int value;
    private Die die;

    public Square(Color color, int value) {
        this.color = color;
        this.value = value;
        die=null;
    }

    public Die getDie() {
        return die;
    }

    public void setDie(Die die) { //if you want to free the square just pass null to this method
        this.die = die;
    }

    boolean isEmpty() {
        return die==null;
    }

    boolean sameColor(Die die) {
        return color==Color.WHITE || die.getColor().equals(color);
    }

    boolean sameValue(Die die) {
        return die.getValue()==value;
    }

}
