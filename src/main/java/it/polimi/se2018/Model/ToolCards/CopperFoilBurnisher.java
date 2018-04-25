package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Moves.Move;

public class CopperFoilBurnisher extends ToolCard {

    public CopperFoilBurnisher(String imagePath, String title, Board board) {
        super(imagePath, title, board);
    }

    @Override
    //devo essere riscuro che move abbia i getter giusti
    //Throw exeption
    public void useCard(Move move) {
        if(move.getPlayer().getMap().isValidNoValue(move.getDie(), move.getRowTo(), move.getColTo())) {
            move.getPlayer().getMap().popDie(move.getRowFrom(), move.getColFrom());
            move.getPlayer().getMap().placeDie(move.getDie(), move.getRowTo(), move.getColTo()));
        }
    }
}

