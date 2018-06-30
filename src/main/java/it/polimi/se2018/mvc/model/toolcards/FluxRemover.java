package it.polimi.se2018.mvc.model.toolcards;

import it.polimi.se2018.client.view.gui.ToolCardGUIHandler;
import it.polimi.se2018.mvc.controller.ToolCardHandler;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.controller.ToolCardCheckerHandler;
import it.polimi.se2018.utils.exceptions.ToolCardException;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.client.view.cli.ToolCardCLIHandler;

public class FluxRemover extends ToolCard {
    private static FluxRemover instance = null;

    public static FluxRemover instance(String imagePath){
        if (instance==null) instance = new FluxRemover(imagePath);
        return instance;
    }

    private FluxRemover(String imagePath) {
        super("Flux Remover", "After drafting, return the die to the Dice Bag and pull 1 die from the bag");
        this.imagePath = imagePath;
    }

    @Override
    public void handle(ToolCardHandler handler, ToolCardMessage message) throws ToolCardException{
        handler.useCard(this, message);
    }
    @Override
    public ToolCardMessage handleView(ToolCardCLIHandler handler, int toolcardnumber) {
        return handler.getPlayerRequests(this, toolcardnumber);
    }

    @Override
    public Boolean handleCheck(ToolCardCheckerHandler handler, boolean isUsed, Player player){
        return handler.checkUsability(this, isUsed, player);
    }

    @Override
    public void handleGUI(ToolCardGUIHandler handler, int toolcardnumber) {
        handler.getPlayerRequests(this, toolcardnumber);
    }
}