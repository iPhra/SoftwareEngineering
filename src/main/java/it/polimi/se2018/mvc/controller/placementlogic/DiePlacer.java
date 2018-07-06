package it.polimi.se2018.mvc.controller.placementlogic;

import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.utils.exceptions.InvalidPlacementException;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.mvc.model.Square;

import java.util.List;

@SuppressWarnings("SimplifiableIfStatement")
/**
 * This class place a die in a specific square and check the condition.
 * Used by the toolcard when move a die or by the standard placement of the turn
 */
public abstract class DiePlacer {
    /**
     * This is the die thata the placer has to place in a square
     */
    final Die die;

    /**
     * This is the square where the placer try to place the die
     */
    final Square square;

    /**
     * This is the window of the player where the placer try to place the die
     */
    final Window window;

    DiePlacer(Die die, Coordinate coordinate, Window window) {
        this.die = die;
        this.window = window;
        this.square = window.getSquare(coordinate);
    }

    /**
     * Check if a die could go in a specific square or not
     * @return true if the die could go, false otherwise
     */
    protected abstract boolean checkCondition ();

    /**
     * Used by method checkCondition in order to check if you can place die
     * without "violating" adjacent dice color
     * @return true if you can place the die, false otherwise
     */
    boolean isColorOk() {
        List<Die> adjacent = window.adjacentDice(new Coordinate(square.getRow(), square.getCol()));
        for (Die near : adjacent){
            if (die.getColor().equals(near.getColor())){
                return false;
            }
        }
        return true;
    }

    /**
     * Used by method placeDie in order to check if you can place die
     * without "violating" adjacent dice value
     * @return true if you can place the die, false otherwise
     */
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

    /**
     * Used by method placeDie in order to check if you can place die
     * without "violating" the rule that the die has to be near another one
     * @return true if you can place the die, false otherwise
     */
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

    /**
     * This method place the die of the placer
     * @throws InvalidPlacementException because you can't place the die in that {@link Square}
     */
    public void placeDie() throws InvalidPlacementException {
        if(!this.checkCondition()) throw new InvalidPlacementException();
        square.setDie(die);
    }
}
