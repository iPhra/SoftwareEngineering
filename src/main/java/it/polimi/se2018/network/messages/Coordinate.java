package it.polimi.se2018.network.messages;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents a pair of integers, identifying a generic coordinate on any system representable as a matrix
 */
public class Coordinate implements Serializable{
    /**
     * This is the x coordinate, or row of the matrix
     */
    private final int row;

    /**
     * This is the y coordinate, or column of the matrix
     */
    private final int col;

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

    public String getDescription() {
        return ("row " + row + " col " + col);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return row == that.row &&
                col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
