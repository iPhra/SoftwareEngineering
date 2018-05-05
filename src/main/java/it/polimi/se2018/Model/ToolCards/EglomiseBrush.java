package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Messages.MoveMessage;

public class EglomiseBrush extends ToolCard {

    public EglomiseBrush(String imagePath, String title, Board board) {
        super(imagePath, title, board);
    }
    @Override
    public void useCard(MoveMessage moveMessage) {
        if(moveMessage.getPlayer().getMap().isValidNoColor(moveMessage.getDie(), moveMessage.getRowTo(), moveMessage.getColTo())) {
            moveMessage.getPlayer().getMap().popDie(moveMessage.getRowFrom(), moveMessage.getColFrom());
            moveMessage.getPlayer().getMap().placeDie(moveMessage.getDie(), moveMessage.getRowTo(), moveMessage.getColTo());
        }
    }
}
