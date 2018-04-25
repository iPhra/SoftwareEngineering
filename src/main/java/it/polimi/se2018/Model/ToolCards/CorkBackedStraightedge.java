package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Moves.Move;

public class CorkBackedStraightedge extends ToolCard {

    public CorkBackedStraightedge(String imagePath, String title, Board board) {
        super(imagePath, title, board);
    }

    @Override
    public void useCard(Move move) {
        //Throw exeption if the Player has already placed a die
        if (move.getPlayer().hasDieInHand()) {
            if (move.getPlayer().getMap().isValidNoValue(move.getPlayer().getDieInHand(), move.getRowTo(), move.getColTo())) {
                board.draftedDieToMap(move.getRowTo(), move.getColTo());
            }
        }
    }
}
