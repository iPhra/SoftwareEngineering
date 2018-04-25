package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Moves.Move;

public class FluxRemover extends ToolCard {

    public FluxRemover(String imagePath, String title, Board board) {
        super(imagePath, title, board);
    }
    @Override
    public void useCard(Move move) {
        //Throw exeption if the Player has already placed a die
        if (move.getPlayer().hasDieInHand()) {
            board.getBag().insertDie(move.getPlayer().getDieInHand());
            move.getPlayer().setDieInHand(null);
            move.getPlayer().setDieInHand(board.getBag().extractDie());
            //darò il colore alla view
            move.getPlayer().getDieInHand().setValue(move.getValue());
            //getValue prende il valore che desidera il giocatore attraverso move (?)
        }
    }
}