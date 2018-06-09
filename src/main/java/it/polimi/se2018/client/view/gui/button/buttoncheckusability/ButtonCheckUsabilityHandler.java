package it.polimi.se2018.client.view.gui.button.buttoncheckusability;

import it.polimi.se2018.client.view.gui.button.*;

public interface ButtonCheckUsabilityHandler {
    boolean checkUsability(ButtonSquare buttonSquare);
    boolean checkUsability(ButtonDraftPool buttonDraftPool);
    boolean checkUsability(ButtonGame buttonGame);
    boolean checkUsability(ButtonToolCard buttonToolCard);
    boolean checkUsability(ButtonRoundTracker buttonRoundTracker);
}
