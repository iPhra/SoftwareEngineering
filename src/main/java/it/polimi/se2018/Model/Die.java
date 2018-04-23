package it.polimi.se2018.Model;

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
    }

    public void flipDie() {
        setValue(7-getValue());
    }

    public Color getColor() {
        return color;
    }
}
