package it.polimi.se2018.Model;

import it.polimi.se2018.Exceptions.NoDieException;
import it.polimi.se2018.Model.Messages.Coordinate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Map implements Iterable<Square>{

    private final String title;
    private final int level; //it's the level of difficulty
    private final String imagePath;
    private final Square[][] matrix;

    public Map(String title, int level, String imagePath, Square[][] matrix) {
        this.title = title;
        this.level = level;
        this.imagePath = imagePath;
        this.matrix = matrix;
    }

    public String getTitle() {
        return title;
    }

    public int getLevel() {
        return level;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Square getSquare(Coordinate coordinate) { return matrix[coordinate.getRow()][coordinate.getCol()]; }

    public int getRows() { return matrix.length; }

    public int getCols() { return matrix[0].length; }

    public Square[][] modelViewCopy() {
        Square[][] result = new Square[getRows()][getCols()];
        for(int i=0; i<getRows(); i++) {
            for(int j=0; j<getCols(); j++) {
                result[i][j]=matrix[i][j].modelViewCopy();
            }
        }
        return result;
    }

    public Die getDie (Coordinate coordinate) {
        return matrix[coordinate.getRow()][coordinate.getCol()].getDie();
    }

    //removes a die from a given position of the map
    public void popDie(Coordinate coordinate) {
        matrix[coordinate.getRow()][coordinate.getCol()].setDie(null);
    }

    //return number of empty slot in a map
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

    private class SquareIterator implements Iterator<Square> {
        private int row;
        private int col;
        SquareIterator(){
            row=0;
            col=0;
        }
        public boolean hasNext() {
            return !(row==matrix.length-1 && col==matrix[0].length-1);
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

    //used by method adjacentOk, returns the adjacent dice of a die
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
}
