package it.polimi.se2018.Controller;

import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Moves.MoveMessage;
import it.polimi.se2018.Utils.Observer;
import it.polimi.se2018.View.ServerView;

public class Controller implements Observer<MoveMessage> {
    private final Board model;
    private final ServerView view;

    public Controller(Board model, ServerView view) {
        super();
        this.model = model;
        this.view = view;
    }

    private void draft(MoveMessage move) {}

    private void pass(MoveMessage move) {}

    private void useToolCard(MoveMessage move) {}

    private void placeDie(MoveMessage move) {}

    //reads player, checks if it's his turn, reads move id, calls right methods
    private void performMove(MoveMessage move){
    }

    @Override
    public void update(MoveMessage moveMessage) {
        performMove(moveMessage);
    }
}
