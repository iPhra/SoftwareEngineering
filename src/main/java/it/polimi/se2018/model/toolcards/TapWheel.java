package it.polimi.se2018.model.toolcards;

import it.polimi.se2018.controller.ToolCardHandler;
import it.polimi.se2018.utils.exceptions.ToolCardException;


import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.view.cli.CLIClientView;
import it.polimi.se2018.view.cli.ToolCardPlayerInputHandler;

public class TapWheel extends ToolCard {

    public TapWheel(String imagePath) {
        super(imagePath, "Tap Wheel", "Move up to two dice of the same color that match the color of a die on the Round Track");
    }

    @Override
    public void handle(ToolCardHandler handler, ToolCardMessage message) throws ToolCardException{
        handler.useCard(this, message);
    }

    @Override
    public ToolCardMessage handleView(ToolCardPlayerInputHandler handler, int toolcardnumber) {
        return handler.getPlayerRequests(this, toolcardnumber);
    }
}

