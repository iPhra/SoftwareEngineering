package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Moves.MoveMessage;

public class CorkBackedStraightedge extends ToolCard {

    public CorkBackedStraightedge(String imagePath, String title, Board board) {
        super(imagePath, title, board);
    }

    @Override
    public void useCard(MoveMessage moveMessage) {
        //Throw exeption if the Player has already placed a die
        if (moveMessage.getPlayer().hasDieInHand()) {
            if (moveMessage.getPlayer().getMap().isValidNoValue(moveMessage.getPlayer().getDieInHand(), moveMessage.getRowTo(), moveMessage.getColTo())) {
                board.draftedDieToMap(moveMessage.getRowTo(), moveMessage.getColTo());
            }
        }
    }
}
