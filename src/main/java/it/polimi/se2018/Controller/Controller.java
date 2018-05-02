package it.polimi.se2018.Controller;

import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Moves.MoveMessage;
import it.polimi.se2018.Utils.Observer;

public class Controller implements Observer<MoveMessage> {

    private final Board model;

    public Controller(Board model) {
        super();
        this.model = model;
    }

    private void draft(MoveMessage move)
    //reads id,
    private synchronized void performMove(MoveMessage move){

    }

    @Override
    public void update(MoveMessage moveMessage) {
        performMove(moveMessage);
    }
}
