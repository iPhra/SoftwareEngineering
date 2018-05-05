package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Messages.MoveMessage;

public abstract class ToolCard {
    protected final boolean alreadyUsed; //true if this tool card has already been used once
    protected boolean usableAfterDraft; //true if this tool card is usable after placing a die
    protected final String imagePath;
    protected final String title;
    protected final Board board;

    protected ToolCard(String imagePath, String title, Board board, boolean alreadyUsed) {
        this.imagePath=imagePath;
        this.title=title;
        this.alreadyUsed = alreadyUsed;
        this.board=board;
    }

    //REMEMBER to set hasPlacedDie and hasUsedCard attributes in Round
    //also, if you call denyNextTurn() from Round, you need to throw an exception if player is in his second turn
    public abstract void useCard(MoveMessage moveMessage); //every specific tool card will implement this method differently

    public boolean isAlreadyUsed() {return alreadyUsed;}

    //creates a new ToolCard identical to this one but with alreadyUsed=true
    public abstract ToolCard setAlreadyUsed();


}
