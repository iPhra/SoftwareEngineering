package it.polimi.se2018.mvc.model.objectives.publicobjectives;

import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.network.messages.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the color diagonals objective card. It extends {@link PublicObjective}
 * Design pattern Singleton is used in this class, because only one instance is used in every running game
 * @author Emilio Imperiali
 */
public class ColorDiagonalsObjective extends PublicObjective {
    private static ColorDiagonalsObjective instance = null;

    /**
     * This is needed in order not to count the same die twice while evaluating points. A cell is true if the
     * corresponding die in the window was already counted while evaluating points, false otherwise
     */
    private final boolean[][] alreadyCounted;

    private ColorDiagonalsObjective(String title){
        super(title);
        description = "Number of diagonally adjacent dice with the same color";
        alreadyCounted = new boolean[4][5];
        resetAlreadyCounted();
    }

    /**
     * @param title it's the title of this card
     * @return a new instance of this card if does not exist, the existing instance otherwise (as expected in the
     * Singleton pattern)
     */
    public static ColorDiagonalsObjective instance(String title){
        if (instance==null) instance = new ColorDiagonalsObjective(title);
        return instance;
    }

    /**
     * Sets all the cells of alreadyCounted as false
     */
    private void resetAlreadyCounted(){
        for (int row=0; row<4; row++) {
            for (int col=0; col<5; col++) {
                alreadyCounted[row][col]=false;
            }
        }
    }

    //returns dice placed in the 2 diagonals below given position

    /**
     * Used by method evalPoints
     * @param player the player whose points must be evaluated
     * @param row it's the row of the given position
     * @param col it's the col of the given position
     * @return squares placed in the 2 diagonals below given position
     */
    private List<Square> belowDiagonalsSquares(Player player, int row, int col){
        ArrayList<Square> belowDiagonals = new ArrayList<>();
        if (row < player.getWindow().getRows()-1){
            if (col > 0 && !player.getWindow().getSquare(new Coordinate(row+1,col-1)).isEmpty() && !alreadyCounted[row+1][col-1]){
                belowDiagonals.add(player.getWindow().getSquare(new Coordinate(row+1,col-1)));
            }
            if (col < player.getWindow().getCols()-1 && !player.getWindow().getSquare(new Coordinate(row+1,col+1)).isEmpty() && !alreadyCounted[row+1][col+1]){
                belowDiagonals.add(player.getWindow().getSquare(new Coordinate(row+1,col+1)));
            }
        }
        return belowDiagonals;
    }

    /**
     * The points are evaluated as it follows: all diagonally adjacent dice with the same color are counted. The result
     * value represents the points given to the player by this objective
     * @param player the player whose points must be evaluated
     * @return the points given by this card to the player
     */
    @Override
    public int evalPoints(Player player){
        Window window = player.getWindow();
        int points = 0;
        for (Square square : window) {
            if (square.getDie() != null) {
                ArrayList<Square> belowDiagonalsSquares = (ArrayList<Square>)belowDiagonalsSquares(player,square.getRow(),square.getCol());
                for (Square belowSquare : belowDiagonalsSquares) {
                    if (belowSquare.getDie().getColor() == square.getDie().getColor()) {
                        points++;
                        alreadyCounted[belowSquare.getRow()][belowSquare.getCol()] = true;
                        if (!alreadyCounted[square.getRow()][square.getCol()]){
                            points++;
                            alreadyCounted[square.getRow()][square.getCol()] = true;
                        }
                    }
                }
            }
        }
        resetAlreadyCounted();
        return points;
    }
}
