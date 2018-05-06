package it.polimi.se2018.Model;

import it.polimi.se2018.Exceptions.InvalidPlacementException;
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

    public Square getSquare(int row, int col) { return matrix[row][col]; }

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

    //removes a die from a given position of the map
    public Die popDie(Coordinate coordinate) {
        Die result = matrix[coordinate.getRow()][coordinate.getCol()].getDie();
        matrix[coordinate.getRow()][coordinate.getCol()].setDie(null);
        return result;
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
}
