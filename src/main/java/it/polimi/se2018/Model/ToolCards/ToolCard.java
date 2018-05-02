package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Moves.MoveMessage;

public abstract class ToolCard {
    protected boolean alreadyUsed; //true if this tool card has already been used once
    protected boolean usableAfterDraft; //true if this tool card is usable after placing a die
    protected String imagePath;
    protected String title;
    protected Board board;

    protected ToolCard(String imagePath, String title, Board board) {
        this.imagePath=imagePath;
        this.title=title;
        alreadyUsed = false;
        this.board=board;
    }

    //REMEMBER to set hasPlacedDie and hasUsedCard attributes in Round
    //also, if you call denyNextTurn() from Round, you need to throw an exception if player is in his second turn
    public abstract void useCard(MoveMessage moveMessage); //every specific tool card will implement this method differently


    public boolean isAlreadyUsed() {return alreadyUsed;}

    public void setAlreadyUsed() {alreadyUsed=true;}
}
