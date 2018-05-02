package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Moves.MoveMessage;

public class FluxBrush extends ToolCard {

    public FluxBrush(String imagePath, String title, Board board) {
        super(imagePath, title, board);
    }
    @Override
    public void useCard(MoveMessage moveMessage) {
        //Throw exception if the Player has already placed a die
        if (moveMessage.getPlayer().hasDieInHand()) {
            moveMessage.getPlayer().getDieInHand().rollDie();
        }
    }
}


