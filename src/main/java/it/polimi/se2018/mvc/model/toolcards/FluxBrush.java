package it.polimi.se2018.mvc.model.toolcards;

import it.polimi.se2018.client.view.gui.ToolCardGUIHandler;
import it.polimi.se2018.mvc.controller.ToolCardHandler;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.controller.ToolCardCheckerHandler;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.client.view.cli.ToolCardPlayerInputHandler;

public class FluxBrush extends ToolCard {
    private static FluxBrush instance = null;

    public static FluxBrush instance(String imagePath){
        if (instance==null) instance = new FluxBrush(imagePath);
        return instance;
    }

    private FluxBrush(String imagePath) {
        super("Flux Brush", "After drafting, re-roll the drafted die");
        this.imagePath = imagePath;
    }

    @Override
    public void handle(ToolCardHandler handler, ToolCardMessage message) {
        handler.useCard(this, message);
    }
    @Override
    public ToolCardMessage handleView(ToolCardPlayerInputHandler handler, int toolcardnumber) {
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


