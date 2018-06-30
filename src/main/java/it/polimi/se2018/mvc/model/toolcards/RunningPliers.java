package it.polimi.se2018.mvc.model.toolcards;

import it.polimi.se2018.client.view.cli.ToolCardCLIHandler;
import it.polimi.se2018.client.view.gui.ToolCardGUIHandler;
import it.polimi.se2018.mvc.controller.ToolCardHandler;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.controller.ToolCardCheckerHandler;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;

public class RunningPliers extends ToolCard {
    private static RunningPliers instance = null;

    public static RunningPliers instance(String imagePath){
        if (instance==null) instance = new RunningPliers(imagePath);
        return instance;
    }

    private RunningPliers(String imagePath) {
        super("Running Pliers", "After your first turn, immediately draft a die");
        this.imagePath = imagePath;
    }

    @Override
    public void handle(ToolCardHandler handler, ToolCardMessage message) {
        handler.useCard(this, message);
    }
    @Override
    public ToolCardMessage handleView(ToolCardCLIHandler handler, int toolCardNumber) {
        return handler.getPlayerRequests(this, toolCardNumber);
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

