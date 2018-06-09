package it.polimi.se2018.client.view.gui.button.buttoncheckusability;

import it.polimi.se2018.client.view.gui.button.*;
import it.polimi.se2018.client.view.gui.stategui.State;
import it.polimi.se2018.client.view.gui.stategui.StateRoundTracker;
import it.polimi.se2018.client.view.gui.stategui.StateTurn;
import it.polimi.se2018.client.view.gui.stategui.statedraftpool.StateToolCardDraft;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateWindowEnd;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateWindowPlace;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateWindowStart;

public interface ButtonCheckUsabilityHandler {
    public Boolean checkUsability(ButtonSquare buttonSquare);
    public Boolean checkUsability(ButtonDraftPool buttonDraftPool);
    public Boolean checkUsability(ButtonGame buttonGame);
    public Boolean checkUsability(ButtonToolCard buttonToolCard);
    public Boolean checkUsability(ButtonRoundTracker buttonRoundTracker);
}
