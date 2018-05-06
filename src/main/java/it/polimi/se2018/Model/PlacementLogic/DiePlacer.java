package it.polimi.se2018.Model.PlacementLogic;

import it.polimi.se2018.Exceptions.InvalidPlacementException;
import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Model.Map;
import it.polimi.se2018.Model.Messages.Coordinate;
import it.polimi.se2018.Model.Square;

import java.util.ArrayList;
import java.util.List;

public abstract class DiePlacer {
    protected Die die;
    protected Coordinate coordinate;
    protected Square square;
    protected Map map;

    protected DiePlacer(Die die, Coordinate coordinate, Map map) {
        this.die = die;
        this.coordinate = coordinate;
        this.map = map;
        this.square = map.getSquare(coordinate.getRow(),coordinate.getCol());
    }

    protected abstract boolean checkCondition ();

    public void placeDie() throws InvalidPlacementException {
        if(!this.checkCondition()) throw new InvalidPlacementException();
        square.setDie(die);
    }

    //used by method checkCondition in order to check if you can place die without "violating" adjacent dice color
    protected boolean isColorOk() {
        List<Die> adjacent = adjacentDice(square.getRow(), square.getCol());
        for (Die near : adjacent){
            if (die.getColor().equals(near.getColor())){
                return false;
            }
        }
        return true;
    }

    //used by method placeDie in order to check if you can place die without "violating" adjacent dice value
    protected boolean isValueOk() {
        List<Die> adjacent= adjacentDice(square.getRow(),square.getCol());
        for (Die near : adjacent){
            if (die.getValue() == near.getValue()){
                return false;
            }
        }
        return true;
    }

    //all dice in diagonal and adjacent to a given die
    protected boolean hasSurroundingDice() {
        List<Die> surrounding = adjacentDice(square.getRow(),square.getCol());
        if (!surrounding.isEmpty()) return true;
        if (square.getRow() > 0 && square.getCol() > 0 && !map.getSquare(square.getRow()-1,square.getCol()-1).isEmpty()) return true;
        if (square.getRow() > 0 && square.getCol() < map.getCols() && !map.getSquare(square.getRow()-1,square.getCol()+1).isEmpty()) return true;
        if (square.getRow() < map.getRows() && square.getCol() > 0 && !map.getSquare(square.getRow()+1,square.getCol()-1).isEmpty()) return true;
        if (square.getRow() < map.getRows() && square.getCol() < map.getCols() && !map.getSquare(square.getRow()+1,square.getCol()+1).isEmpty()) return true;
        return false;
    }

    //used by method adjacentOk, returns the adjacent dice of a die
    private List<Die> adjacentDice(int row, int col){
        ArrayList<Die> adjacent = new ArrayList<>();
        int rows = map.getRows();
        int cols = map.getCols();
        if (row > 0) adjacent.add(map.getSquare(row-1,col).getDie());
        if (row < rows-1) adjacent.add(map.getSquare(row+1,col).getDie());
        if (col > 0) adjacent.add(map.getSquare(row,col-1).getDie());
        if (col < cols-1) adjacent.add(map.getSquare(row,col+1).getDie());
        return adjacent;
    }

}
