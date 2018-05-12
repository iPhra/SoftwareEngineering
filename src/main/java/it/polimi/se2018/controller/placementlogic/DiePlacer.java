package it.polimi.se2018.controller.placementlogic;

import it.polimi.se2018.utils.exceptions.InvalidPlacementException;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Map;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.model.Square;

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
        this.square = map.getSquare(coordinate);
    }

    protected abstract boolean checkCondition ();

    public void placeDie() throws InvalidPlacementException {
        if(!this.checkCondition()) throw new InvalidPlacementException();
        square.setDie(die);
    }

    //used by method checkCondition in order to check if you can place die without "violating" adjacent dice color
    protected boolean isColorOk() {
        List<Die> adjacent = map.adjacentDice(new Coordinate(square.getRow(), square.getCol()));
        for (Die near : adjacent){
            if (die.getColor().equals(near.getColor())){
                return false;
            }
        }
        return true;
    }

    //used by method placeDie in order to check if you can place die without "violating" adjacent dice value
    protected boolean isValueOk() {
        List<Die> adjacent= map.adjacentDice(new Coordinate(square.getRow(),square.getCol()));
        for (Die near : adjacent){
            if (die.getValue() == near.getValue()){
                return false;
            }
        }
        return true;
    }

    //all dice in diagonal and adjacent to a given die
    protected boolean hasSurroundingDice() {
        List<Die> surrounding = map.adjacentDice(new Coordinate(square.getRow(), square.getCol()));
        if (!surrounding.isEmpty()) return true;
        if (square.getRow() > 0 && square.getCol() > 0 && !map.getSquare(new Coordinate(square.getRow() - 1, square.getCol() - 1)).isEmpty())
            return true;
        if (square.getRow() > 0 && square.getCol() < map.getCols() && !map.getSquare(new Coordinate(square.getRow() - 1, square.getCol() + 1)).isEmpty())
            return true;
        return square.getRow() < map.getRows() && square.getCol() > 0 && !map.getSquare(new Coordinate(square.getRow() + 1, square.getCol() - 1)).isEmpty() || square.getRow() < map.getRows() && square.getCol() < map.getCols() && !map.getSquare(new Coordinate(square.getRow() + 1, square.getCol() + 1)).isEmpty();
    }

}
