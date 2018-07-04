package it.polimi.se2018.mvc.controller.placementlogic;

import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.utils.exceptions.InvalidPlacementException;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.mvc.model.Square;

import java.util.List;

@SuppressWarnings("SimplifiableIfStatement")
public abstract class DiePlacer {
    final Die die;
    final Square square;
    final Window window;

    DiePlacer(Die die, Coordinate coordinate, Window window) {
        this.die = die;
        this.window = window;
        this.square = window.getSquare(coordinate);
    }

    protected abstract boolean checkCondition ();

    //used by method checkCondition in order to check if you can place die without "violating" adjacent dice color
    boolean isColorOk() {
        List<Die> adjacent = window.adjacentDice(new Coordinate(square.getRow(), square.getCol()));
        for (Die near : adjacent){
            if (die.getColor().equals(near.getColor())){
                return false;
            }
        }
        return true;
    }

    //used by method placeDie in order to check if you can place die without "violating" adjacent dice value
    boolean isValueOk() {
        List<Die> adjacent= window.adjacentDice(new Coordinate(square.getRow(),square.getCol()));
        for (Die near : adjacent){
            if (die.getValue() == near.getValue()){
                return false;
            }
        }
        return true;
    }

    //all dice in diagonal and adjacent to a given die
    boolean hasSurroundingDice() {
        List<Die> surrounding = window.adjacentDice(new Coordinate(square.getRow(), square.getCol()));
        if (!surrounding.isEmpty()) return true;
        if (square.getRow() > 0 && square.getCol() > 0 && !window.getSquare(new Coordinate(square.getRow() - 1, square.getCol() - 1)).isEmpty())
            return true;
        if (square.getRow() > 0 && square.getCol() < window.getCols() - 1 && !window.getSquare(new Coordinate(square.getRow() - 1, square.getCol() + 1)).isEmpty())
            return true;
        if (square.getRow() < window.getRows() - 1 && square.getCol() > 0 && !window.getSquare(new Coordinate(square.getRow() + 1, square.getCol() - 1)).isEmpty())
            return true;
        return square.getRow() < window.getRows() - 1 && square.getCol() < window.getCols() - 1 && !window.getSquare(new Coordinate(square.getRow() + 1, square.getCol() + 1)).isEmpty();
    }

    public void placeDie() throws InvalidPlacementException {
        if(!this.checkCondition()) throw new InvalidPlacementException();
        square.setDie(die);
    }
}
