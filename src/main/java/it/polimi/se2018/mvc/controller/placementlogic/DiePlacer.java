package it.polimi.se2018.mvc.controller.placementlogic;

import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.utils.exceptions.InvalidPlacementException;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.mvc.model.Square;

import java.util.List;


/**
 * This class is used to check if a die can be placed in a specific square, and to place it.
 * Used by {@link it.polimi.se2018.mvc.controller.Controller} and {@link it.polimi.se2018.mvc.model.toolcards.ToolCard}.
 */
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

    /**
     * Checks if a die can be placed in the square
     *
     * @return {@code true} if the condition is met
     */
    protected abstract boolean checkCondition();

    /**
     * Checks if the color condition on adjacent dice is met
     * @return {@code true} if the color condition is met
     */
    boolean isColorOk() {
        List<Die> adjacent = window.adjacentDice(new Coordinate(square.getRow(), square.getCol()));
        for (Die near : adjacent) {
            if (die.getColor().equals(near.getColor())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the value condition on adjacent dice is met
     * @return {@code true} if the value condition is met
     */
    boolean isValueOk() {
        List<Die> adjacent = window.adjacentDice(new Coordinate(square.getRow(), square.getCol()));
        for (Die near : adjacent) {
            if (die.getValue() == near.getValue()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the condition on adjacent dice is met
     * @return {@code true} if the condition is met
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
     * This method places the die.
     *
     * @throws InvalidPlacementException if the condition is not met
     */
    public void placeDie() throws InvalidPlacementException {
        if (!this.checkCondition()) throw new InvalidPlacementException();
        square.setDie(die);
    }
}
