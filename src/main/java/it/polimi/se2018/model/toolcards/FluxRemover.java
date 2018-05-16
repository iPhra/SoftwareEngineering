package it.polimi.se2018.model.toolcards;

import it.polimi.se2018.controller.ToolCardHandler;
import it.polimi.se2018.utils.exceptions.ToolCardException;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.view.cli.CLIClientView;

public class FluxRemover extends ToolCard {

    public FluxRemover(String imagePath) {
        super(imagePath, "Flux Remover", "After drafting, return the die to the Dice Bag and pull 1 die from the bag");
    }

    @Override
    public void handle(ToolCardHandler handler, ToolCardMessage message) throws ToolCardException{
        handler.useCard(this, message);
    }

    public ToolCardMessage getPlayerRequests(int playerID, CLIClientView clientView, int toolcardnumber) {
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, toolcardnumber);
        int value = clientView.getValueDie();
        toolCardMessage.addValue(value);
        return  toolCardMessage;
    }
}