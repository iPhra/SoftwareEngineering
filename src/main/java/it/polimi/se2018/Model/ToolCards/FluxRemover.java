package it.polimi.se2018.Model.ToolCards;

import it.polimi.se2018.Model.Board;
import it.polimi.se2018.Model.Messages.ToolCardMessage;

public class FluxRemover extends ToolCard {

    public FluxRemover(String imagePath, String title, Board board, boolean alreadyUsed) {
        super(imagePath, title, board, alreadyUsed);
    }
    @Override
    public void useCard(ToolCardMessage toolCardMessage) {
        //l'if dovrebbe essere il complemento, se è vero lancia eccezione, altrimenti esegue il metodo
        if (toolCardMessage.getPlayer().hasDieInHand()) {
            board.getBag().insertDie(toolCardMessage.getPlayer().getDieInHand());
            toolCardMessage.getPlayer().setDieInHand(board.getBag().extractDie());
            //darò il colore alla view
            toolCardMessage.getPlayer().getDieInHand().setValue(toolCardMessage.getValue());
            //getValue prende il valore che desidera il giocatore attraverso moveMessage (?)
        }
    }

    @Override
    public ToolCard setAlreadyUsed() {
        return null;
    }
}