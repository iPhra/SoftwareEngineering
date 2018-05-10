package it.polimi.se2018.network.messages;

public class Coordinate {
    private final int row; //This is x
    private final int col; //This is y

    public Coordinate(int row, int col) {
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
