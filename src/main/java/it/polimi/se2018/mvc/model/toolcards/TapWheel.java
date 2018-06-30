package it.polimi.se2018.mvc.model.toolcards;

import it.polimi.se2018.client.view.gui.ToolCardGUIHandler;
import it.polimi.se2018.mvc.controller.ToolCardHandler;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.controller.ToolCardCheckerHandler;
import it.polimi.se2018.utils.exceptions.ChangeActionException;
import it.polimi.se2018.utils.exceptions.HaltException;
import it.polimi.se2018.utils.exceptions.ToolCardException;


import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.client.view.cli.ToolCardCLIHandler;

public class TapWheel extends ToolCard {
    private static TapWheel instance = null;

    public static TapWheel instance(String imagePath){
        if (instance==null) instance = new TapWheel(imagePath);
        return instance;
    }

    private TapWheel(String imagePath) {
        super("Tap Wheel", "Move up to two dice of the same color that match the color of a die on the Round Tracker");
        this.imagePath = imagePath;
    }

    @Override
    public void handle(ToolCardHandler handler, ToolCardMessage message) throws ToolCardException{
        handler.useCard(this, message);
    }

    @Override
    public ToolCardMessage handleView(ToolCardCLIHandler handler, int toolCardNumber) throws HaltException, ChangeActionException {
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

