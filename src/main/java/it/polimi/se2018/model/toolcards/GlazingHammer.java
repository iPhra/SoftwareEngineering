package it.polimi.se2018.model.toolcards;

import it.polimi.se2018.controller.ToolCardHandler;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;

public class GlazingHammer extends ToolCard {

    public GlazingHammer(String imagePath, String title, boolean alreadyUsed) {
        super(imagePath, title);
    }

    @Override
    public void handle(ToolCardHandler handler, ToolCardMessage message) {
        handler.useCard(this, message);
    }
}
