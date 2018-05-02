package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Moves.MoveMessage;

public class CopperFoilBurnisher extends ToolCard {

    public CopperFoilBurnisher(String imagePath, String title, Board board) {
        super(imagePath, title, board);
    }

    @Override
    //devo essere riscuro che moveMessage abbia i getter giusti
    //Throw exeption
    public void useCard(MoveMessage moveMessage) {
        if(moveMessage.getPlayer().getMap().isValidNoValue(moveMessage.getDie(), moveMessage.getRowTo(), moveMessage.getColTo())) {
            moveMessage.getPlayer().getMap().popDie(moveMessage.getRowFrom(), moveMessage.getColFrom());
            moveMessage.getPlayer().getMap().placeDie(moveMessage.getDie(), moveMessage.getRowTo(), moveMessage.getColTo());
        }
    }
}

