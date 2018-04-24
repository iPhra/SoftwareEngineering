package it.polimi.se2018.Model;

import java.util.ArrayList;

public class Map {

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
        ArrayList<Die> adjacent= new ArrayList<Die>();
        if (row > 0) adjacent.add(matrix[row-1][col].getDie());
        if (row < matrix.length) adjacent.add(matrix[row+1][col].getDie());
        if (col > 0) adjacent.add(matrix[row][col-1].getDie());
        if (col < matrix[0].length) adjacent.add(matrix[row][col+1].getDie());
        return adjacent;
    };
    //used by method placeDie in order to check if you can place die without "violating" adjacent dice
    private boolean adjacentOk(Die die, int row, int col) {
        ArrayList<Die> adjacent= adjacentDice(row,col);
        for (int i=0; i<adjacent.size(); i++){
            if (die.getColor().equals(adjacent.get(i).getColor()) || die.getValue() == adjacent.get(i).getValue()){
                return false;
            }
        }
        return true;
    }

    //row and col are indexes, they start from 0
    private boolean isOnEdge(int row, int col) {
        return (row==matrix.length-1 || row == 0 || col==matrix[0].length-1 || col == 0);
    }


    private boolean isValidMove(Die die, int row, int col) {
        return matrix[row][col].isEmpty() && matrix[row][col].sameColor(die) && matrix[row][col].sameValue(die) && adjacentOk(die,row,col);
    }

    //places a die in a given position of the map
    public void placeDie(Die die, int row, int col) {
        matrix[row][col].setDie(die);
    }

    //removes a die from a given position of the map
    public Die popDie(int row, int col) {
        Die result = matrix[row][col].getDie();
        matrix[row][col].setDie(null);
        return result;
    }
}
