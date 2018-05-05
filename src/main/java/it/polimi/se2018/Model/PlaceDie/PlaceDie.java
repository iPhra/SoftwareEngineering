package it.polimi.se2018.Model.PlaceDie;

import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Model.Map;
import it.polimi.se2018.Model.Messages.Coordinate;
import it.polimi.se2018.Model.Square;

import java.util.ArrayList;

public abstract class PlaceDie {
    protected Die die;
    protected Coordinate coordinate;
    protected Square square;
    protected Map map;

    protected PlaceDie() {
        this.die = die;
        this.coordinate = coordinate;
        this.map = map;
        this.square = map.getSquare(coordinate.getRow(),coordinate.getCol());
    }

    public boolean checkCondition (){
        return true;
    }
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

}
