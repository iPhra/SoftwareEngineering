package it.polimi.se2018.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Map implements  Iterable<Square>{

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

    public Square[][] getMatrix() {
        return matrix;
    }

    //used by method adjacentOk, returns the adjacent dice of a die
    private ArrayList<Die> adjacentDice(int row, int col){
        ArrayList<Die> adjacent = new ArrayList<Die>();
        if (row > 0) adjacent.add(matrix[row-1][col].getDie());
        if (row < matrix.length) adjacent.add(matrix[row+1][col].getDie());
        if (col > 0) adjacent.add(matrix[row][col-1].getDie());
        if (col < matrix[0].length) adjacent.add(matrix[row][col+1].getDie());
        return adjacent;
    }

    //all dice in diagonal and adjacent to a given die
    private boolean hasSurroundingDice(int row, int col) {
        ArrayList<Die> surrounding = adjacentDice(row,col);
        if (!surrounding.isEmpty()) return true;
        if (row > 0 && col > 0 && !matrix[row-1][col-1].isEmpty()) return true;
        if (row > 0 && col < matrix[0].length && !matrix[row-1][col+1].isEmpty()) return true;
        if (row < matrix.length && col > 0 && !matrix[row+1][col-1].isEmpty()) return true;
        if (row < matrix.length && col < matrix[0].length && !matrix[row+1][col+1].isEmpty()) return true;
        return false;
    }

    //used by method placeDie in order to check if you can place die without "violating" adjacent dice color
    private boolean isColorOk(Die die, int row, int col) {
        ArrayList<Die> adjacent = adjacentDice(row,col);
        for (int i=0; i<adjacent.size(); i++){
            if (die.getColor().equals(adjacent.get(i).getColor())){
                return false;
            }
        }
        return true;
    }

    //used by method placeDie in order to check if you can place die without "violating" adjacent dice value
    private boolean isValueOk(Die die, int row, int col) {
        ArrayList<Die> adjacent= adjacentDice(row,col);
        for (int i=0; i<adjacent.size(); i++){
            if (die.getValue() == adjacent.get(i).getValue()){
                return false;
            }
        }
        return true;
    }

    //row and col are indexes, they start from 0
    private boolean isOnEdge(int row, int col) {
        return (row==matrix.length-1 || row == 0 || col==matrix[0].length-1 || col == 0);
    }

    //This method check all condition to put a die in a square
    public boolean isValidMove(Die die, int row, int col) {
        return matrix[row][col].isEmpty() && matrix[row][col].sameColor(die) && matrix[row][col].sameValue(die) && isValueOk(die,row,col) && isColorOk(die,row,col) && hasSurroundingDice(row,col);
    }
    //This method do NOT check color condition
    public boolean isValidNoColor(Die die, int row, int col) {
        return matrix[row][col].isEmpty() && matrix[row][col].sameValue(die) && isValueOk(die,row,col) && hasSurroundingDice(row,col);
    }
    //This method do NOT check value condition
    public boolean isValidNoValue(Die die, int row, int col) {
        return matrix[row][col].isEmpty() && matrix[row][col].sameColor(die) && isColorOk(die,row,col) && hasSurroundingDice(row,col);
    }
    //This method do NOT check that the die is near another die
    public boolean isValidNoPosition(Die die, int row, int col) {
        return matrix[row][col].isEmpty() && matrix[row][col].sameColor(die) && matrix[row][col].sameValue(die) && isValueOk(die,row,col) && isColorOk(die,row,col);
    }

    //places a die in a given position of the map, validity is checked by whoever calls this method
    public void placeDie(Die die, int row, int col) {
        matrix[row][col].setDie(die);
    }

    //places a die on the edge of the map, validity is checked by whoever calls this method
    public void placeDieOnEdge(Die die, int row, int col) throws Exception{ //da sistemare l'eccezione
        if (!isOnEdge(row,col)) throw new Exception();
        placeDie(die,row,col);
    }

    //removes a die from a given position of the map
    public Die popDie(int row, int col) {
        Die result = matrix[row][col].getDie();
        matrix[row][col].setDie(null);
        return result;
    }

    public Iterator<Square> iterator(){
        return new SquareIterator(this);
    }

    private static class SquareIterator implements Iterator<Square> {
        private Map m;
        private int row;
        private int col;
        SquareIterator(Map map){
            m=map;
            row=0;
            col=0;
        }
        public boolean hasNext() {
            return !(row==m.getMatrix().length-1 && col==m.getMatrix()[0].length-1);
        }
        public Square next() throws NoSuchElementException{
            if (!hasNext()) throw new NoSuchElementException("Matrix.terms");
            else if (col<m.getMatrix()[0].length-1) {
                col++;

            }
            else {
                col=0;
                row++;
            }
            return m.getMatrix()[row][col];

        }
    }
}
