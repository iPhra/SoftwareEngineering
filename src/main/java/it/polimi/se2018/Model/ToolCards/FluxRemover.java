package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Messages.MoveMessage;

public class FluxRemover extends ToolCard {

    public FluxRemover(String imagePath, String title, Board board) {
        super(imagePath, title, board);
    }
    @Override
    public void useCard(MoveMessage moveMessage) {
        //Throw exeption if the Player has already placed a die
        if (moveMessage.getPlayer().hasDieInHand()) {
            board.getBag().insertDie(moveMessage.getPlayer().getDieInHand());
            moveMessage.getPlayer().setDieInHand(null);
            moveMessage.getPlayer().setDieInHand(board.getBag().extractDie());
            //darò il colore alla view
            moveMessage.getPlayer().getDieInHand().setValue(moveMessage.getValue());
            //getValue prende il valore che desidera il giocatore attraverso moveMessage (?)
        }
    }
}