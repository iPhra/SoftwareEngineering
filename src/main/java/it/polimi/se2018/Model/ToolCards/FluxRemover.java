package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Exceptions.DieException;
import it.polimi.se2018.Exceptions.NoDieException;
import it.polimi.se2018.Exceptions.ToolCardException;
import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Messages.ToolCardMessage;

public class FluxRemover extends ToolCard {

    public FluxRemover(String imagePath, String title, Board board, boolean alreadyUsed) {
        super(imagePath, title, board, alreadyUsed);
    }
    @Override
    //After drafting, return the die to the Dice Bag and pull 1 die from the bag
    public void useCard(ToolCardMessage toolCardMessage) throws ToolCardException {
        try {
            if (!toolCardMessage.getPlayer().hasDieInHand()) {
                throw new ToolCardException("Non hai un dado in mano!");
            }
            board.getBag().insertDie(toolCardMessage.getPlayer().getDieInHand());
            toolCardMessage.getPlayer().setDieInHand(board.getBag().extractDie());
            //darò il colore alla view
            toolCardMessage.getPlayer().getDieInHand().setValue(toolCardMessage.getValue());
            //getValue prende il valore che desidera il giocatore attraverso moveMessage (?)
        }
        catch (NoDieException e) {
            throw new ToolCardException("La bag non ha dadi");
        }
        catch (DieException e) {
            throw new ToolCardException("Valore del dado non valido!");
        }
    }

    @Override
    public ToolCard setAlreadyUsed() {
        return null;
    }
}