package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Exceptions.DieException;
import it.polimi.se2018.Exceptions.NoDieException;
import it.polimi.se2018.Exceptions.ToolCardException;
import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Die;
import it.polimi.se2018.Model.Messages.ToolCardMessage;

public class GrozingPliers extends ToolCard {

    public GrozingPliers(String imagePath, String title, Board board, boolean alreadyUsed) {
        super(imagePath, title, board, alreadyUsed);
    }

    @Override
    //After drafting, increase or decrease the value of the drafted die by 1
    public void useCard(ToolCardMessage toolCardMessage) throws ToolCardException{
        if (!toolCardMessage.getPlayer().hasDieInHand()) {
            throw new ToolCardException("Non hai un dado in mano!");
        }
        if (toolCardMessage.getValue() != 1 || toolCardMessage.getValue() != -1) {
        //This toolcard change the value of the die by +1 or -1, other value are not allowed
            throw  new ToolCardException("Non puoi modificare nel modo indicato il dado! Puoi scegliere solo +1 o -1");
        }
        try {
            Die dieToChange = toolCardMessage.getPlayer().getDieInHand();
            dieToChange.setValue(dieToChange.getValue() + toolCardMessage.getValue());
        }
        catch (DieException e) {
            throw new ToolCardException("Il dado assume un valore troppo alto/basso");
        }
    }

    @Override
    public ToolCard setAlreadyUsed() {
        return null;
    }

}
