package it.polimi.se2018;

import it.polimi.se2018.model.Color;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Square;
import it.polimi.se2018.network.messages.Coordinate;

public class Database {
    private Square[][] matrix;


    public void initBasicMatrix(){
        matrix = new Square[4][5];
        for(int row=0;row<4;row++){
            for(int col=0;col<5;col++){
                matrix[row][col] = new Square(Color.WHITE,0,new Coordinate(row,col));
            }
        }
    }

    public void sixSameColoredDice(Color color){
        initBasicMatrix();
        matrix[1][2].setDie(new Die(2, color));
        matrix[0][3].setDie(new Die(4, color));
        matrix[2][1].setDie(new Die(5, color));
        matrix[3][4].setDie(new Die(3, color));
        matrix[0][1].setDie(new Die(5, color));
        matrix[1][4].setDie(new Die(2, color));

    }

    public Square[][] getMatrix() {
        return matrix;
    }
}
