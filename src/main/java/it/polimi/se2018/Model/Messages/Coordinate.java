package it.polimi.se2018.Model.Messages;

public class Coordinate {
    private final int row;
    private final int col;

    public Coordinate(int row, int col) {
        //aggiungere controlli sulla validità
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

}
