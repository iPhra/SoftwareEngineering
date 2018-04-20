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

    Die getDie() {
        return die;
    }

    void setDie(Die die) {
        this.die = die;
    }

    boolean isEmpty() {
        return die==null;
    }

    boolean sameColor(Die die) {
        return die.getColor()==Color.WHITE || die.getColor().equals(color);
    }

    boolean sameValue(Die die) {
        return die.getValue()==(value);
    }
}
