package it.polimi.se2018.Model;

public class Map {

    private final String title;
    private final int level;
    private final String imagePath;
    private final Square[][] matrix; //la mastrice degli Square che costituiscono la Map

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
    //controlla se gli Square adiacenti vanno bene per mettere lì quel dado
    private boolean adjacentOk(Die die, int row, int col) {return true;} //abbiamo scritto questo return perché se no dà errore e non ci lascia scrivere il prototipo del metodo

    private boolean isOnEdge(int row, int col) {
        return row==matrix[0].length || col==matrix.length;
    }

    //controlla se la mossa è valida
    private boolean isValidMove(Die die, int row, int col) {
        return matrix[row][col].isEmpty() && matrix[row][col].sameColor(die) && matrix[row][col].sameValue(die) && adjacentOk(die,row,col);
    }

    //piazza il dado in una determinata posizione della map
    public void placeDie(Die die, int row, int col) {
        matrix[row][col].setDie(die);
    }

    public Die popDie(int row, int col) {
        Die result = matrix[row][col].getDie();
        matrix[row][col].setDie(null);
        return result;
    }
}
