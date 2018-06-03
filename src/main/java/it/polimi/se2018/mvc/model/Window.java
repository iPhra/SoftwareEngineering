package it.polimi.se2018.mvc.model;

import it.polimi.se2018.network.messages.Coordinate;

import java.io.Serializable;
import java.util.*;

/**
 * This class represents a window of the game
 * @author Edoardo Lamonaca
 */
public class Window implements Iterable<Square>, Serializable {
    /**
     * This is the name of this window in the game
     */
    private final String title;

    /**
     * This is the value of favour points that give to the Player
     * if he chooses this window. It's the level of difficulty
     */
    private final int level;

    /**
     * This is the matrix of Square that composite the window
     */
    private final Square[][] matrix;

    private final String levelPath;

    private class SquareIterator implements Iterator<Square> {
        private int row;
        private int col;
        SquareIterator(){
            row=0;
            col=0;
        }
        public boolean hasNext() {
            return (row<=matrix.length-1 && col<=matrix[0].length-1);
        }

        public Square next() {
            if (!hasNext()) throw new NoSuchElementException("No more elements available in the iterator");
            Square res = matrix[row][col];
            if (col<matrix[0].length-1) {
                col++;
            }
            else {
                col=0;
                row++;
            }
            return res;
        }

    }

    public Window(String title, int level, Square[][] matrix, String levelPath) {
        this.title = title;
        this.level = level;
        this.matrix = matrix;
        this.levelPath = levelPath;
    }

    public String getTitle() {
        return title;
    }

    public int getLevel() {
        return level;
    }

    public Square getSquare(Coordinate coordinate) { return matrix[coordinate.getRow()][coordinate.getCol()]; }

    public int getRows() { return matrix.length; }

    public int getCols() { return matrix[0].length; }

    public String getLevelPath() {
        return levelPath;
    }

    public Square[][] modelViewCopy() {
        Square[][] result = new Square[getRows()][getCols()];
        for(int i=0; i<getRows(); i++) {
            for(int j=0; j<getCols(); j++) {
                result[i][j]=matrix[i][j].modelViewCopy();
            }
        }
        return result;
    }

    /**
     * Return the die with the given coordinate in the Window
     * @param coordinate is the coordinate that you ask to show
     * @return the die in the given position
     */
    public Die getDie (Coordinate coordinate) {
        return matrix[coordinate.getRow()][coordinate.getCol()].getDie();
    }

    /**
     *
     * @return return value of empty slot in a window
     */
    public int countEmptySlots() {
        int countSlot = 0;
        for (Square[] squares : matrix) {
            for (Square square : squares) {
                if (square.isEmpty()) countSlot++;
            }
        }
        return countSlot;
    }

    public Iterator<Square> iterator(){
        return new SquareIterator();
    }

    /**
     * Used by method adjacentOk
     * @param coordinate of a Square. You have to cechk the position near it
     * @return returns the adjacent dice of a die
     */
    public List<Die> adjacentDice(Coordinate coordinate) {
        int row = coordinate.getRow();
        int col = coordinate.getCol();
        ArrayList<Die> adjacent = new ArrayList<>();
        int rows = getRows();
        int cols = getCols();
        Coordinate cor1 = new Coordinate(row - 1, col);
        Coordinate cor2 = new Coordinate(row + 1, col);
        Coordinate cor3 = new Coordinate(row, col - 1);
        Coordinate cor4 = new Coordinate (row, col + 1);
        if (row > 0 && !getSquare(cor1).isEmpty()) adjacent.add(getSquare(cor1).getDie());
        if (row < rows - 1 && !getSquare(cor2).isEmpty()) adjacent.add(getSquare(cor2).getDie());
        if (col > 0 && !getSquare(cor3).isEmpty()) adjacent.add(getSquare(cor3).getDie());
        if (col < cols - 1 && !getSquare(cor4).isEmpty()) adjacent.add(getSquare(cor4).getDie());
        return adjacent;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Square[] row : matrix) {
            for (Square square : row) {
                result.append(square);
            }
            result.append("\n");
        }
        return result.toString();
    }
}
