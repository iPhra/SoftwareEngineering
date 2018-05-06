package it.polimi.se2018.Model.PlacementLogic;

import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Model.Map;
import it.polimi.se2018.Model.Messages.Coordinate;
import it.polimi.se2018.Model.Square;

import java.util.ArrayList;

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

    public abstract boolean checkCondition ();

    public void placeDie() {
        if(this.checkCondition()) {
            square.setDie(die);
        }
    }

    //used by method checkCondition in order to check if you can place die without "violating" adjacent dice color
    public boolean isColorOk() {
        ArrayList<Die> adjacent = map.adjacentDice(square.getRow(), square.getCol());
        for (int i=0; i<adjacent.size(); i++){
            if (die.getColor().equals(adjacent.get(i).getColor())){
                return false;
            }
        }
        return true;
    }

    //used by method placeDie in order to check if you can place die without "violating" adjacent dice value
    public boolean isValueOk() {
        ArrayList<Die> adjacent= map.adjacentDice(square.getRow(),square.getCol());
        for (int i=0; i<adjacent.size(); i++){
            if (die.getValue() == adjacent.get(i).getValue()){
                return false;
            }
        }
        return true;
    }
    //all dice in diagonal and adjacent to a given die
    public boolean hasSurroundingDice() {
        ArrayList<Die> surrounding = map.adjacentDice(square.getRow(),square.getCol());
        if (!surrounding.isEmpty()) return true;
        if (square.getRow() > 0 && square.getCol() > 0 && !map.getSquare(square.getRow()-1,square.getCol()-1).isEmpty()) return true;
        if (square.getRow() > 0 && square.getCol() < map.getCols() && !map.getSquare(square.getRow()-1,square.getCol()+1).isEmpty()) return true;
        if (square.getRow() < map.getRows() && square.getCol() > 0 && !map.getSquare(square.getRow()+1,square.getCol()-1).isEmpty()) return true;
        if (square.getRow() < map.getRows() && square.getCol() < map.getCols() && !map.getSquare(square.getRow()+1,square.getCol()+1).isEmpty()) return true;
        return false;
    }

    //row and col are indexes, they start from 0
    public boolean isOnEdge() {
        return (square.getRow()==map.getRows()-1 || square.getRow() == 0 || square.getCol()==map.getCols()-1 || square.getCol() == 0);
    }
}
