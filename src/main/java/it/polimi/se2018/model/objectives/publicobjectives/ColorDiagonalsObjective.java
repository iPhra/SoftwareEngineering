package it.polimi.se2018.model.objectives.publicobjectives;

import it.polimi.se2018.model.Map;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.Square;
import it.polimi.se2018.network.messages.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class ColorDiagonalsObjective extends PublicObjective {
    private static ColorDiagonalsObjective instance = null;
    private boolean[][] alreadyCounted;  //needed in order not to count the same die twice while evaluating points

    private ColorDiagonalsObjective(String imagePath, String title){
        super(imagePath,title);
        alreadyCounted = new boolean[4][5];
        resetAlreadyCounted();
    }

    private static synchronized ColorDiagonalsObjective createInstance(String imagePath, String title){
        if (instance==null) instance = new ColorDiagonalsObjective(imagePath, title);
        return instance;
    }

    public static ColorDiagonalsObjective instance(String imagePath, String title){
        if (instance==null) createInstance(imagePath, title);
        return instance;
    }


    private void resetAlreadyCounted(){
        for (int row=0; row<4; row++) {
            for (int col=0; col<5; col++) {
                alreadyCounted[row][col]=false;
            }
        }
    }

    //returns dice placed in the 2 diagonals below given position
    private List<Square> belowDiagonalsSquares(Player player, int row, int col){
        ArrayList<Square> belowDiagonals = new ArrayList<>();
        if (row < player.getMap().getRows()-1){
            if (col > 0 && !player.getMap().getSquare(new Coordinate(row+1,col-1)).isEmpty() && !alreadyCounted[row+1][col-1]){
                belowDiagonals.add(player.getMap().getSquare(new Coordinate(row+1,col-1)));
                //alreadyCounted[row+1][col-1] = true;
            }
            if (col < player.getMap().getCols()-1 && !player.getMap().getSquare(new Coordinate(row+1,col+1)).isEmpty() && !alreadyCounted[row+1][col+1]){
                belowDiagonals.add(player.getMap().getSquare(new Coordinate(row+1,col+1)));
                //alreadyCounted[row+1][col+1] = true;
            }
        }
        return belowDiagonals;
    }

    @Override
    public int evalPoints(Player player){
        Map map = player.getMap();
        int points = 0;
        for (Square square : map) {
            if (square.getDie() != null) {
                ArrayList<Square> belowDiagonalsSquares = (ArrayList<Square>)belowDiagonalsSquares(player,square.getRow(),square.getCol());
                for (Square belowSquare : belowDiagonalsSquares) {
                    if (belowSquare.getDie().getColor() == square.getDie().getColor()) {
                        points++;
                        if (!alreadyCounted[belowSquare.getRow()][belowSquare.getCol()])
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
