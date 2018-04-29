package it.polimi.se2018.Controller;

import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Moves.Move;
import it.polimi.se2018.Utils.Observer;

public class Controller implements Observer<Move> {

    private final Board model;

    public Controller(Board model) {
        super();
        this.model = model;
    }

    private synchronized void performMove(Move move){
    }

    @Override
    public void update(Move move) {
        performMove(move);
    }
}
