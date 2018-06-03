package it.polimi.se2018.client.view.gui.button;

import it.polimi.se2018.client.view.gui.stategui.State;
import it.polimi.se2018.client.view.gui.stategui.StateRoundTracker;
import it.polimi.se2018.client.view.gui.stategui.StateTurn;
import it.polimi.se2018.client.view.gui.stategui.statedraftpool.StateToolCardDraft;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateWindowEnd;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateWindowPlace;
import it.polimi.se2018.client.view.gui.stategui.statewindow.StateWindowStart;

public interface ButtonCheckUsabilityHandler {
    public Boolean checkUsability(ButtonSquare buttonSquare, StateToolCardDraft stateToolCardDraft);
    public Boolean checkUsability(ButtonSquare buttonSquare, StateWindowEnd stateWindowEnd);
    public Boolean checkUsability(ButtonSquare buttonSquare, StateWindowPlace stateWindowPlace);
    public Boolean checkUsability(ButtonSquare buttonSquare, StateWindowStart stateWindowStart);
    public Boolean checkUsability(ButtonSquare buttonSquare, StateRoundTracker stateRoundTracker);
    public Boolean checkUsability(ButtonSquare buttonSquare, StateTurn stateTurn);

    public Boolean checkUsability(ButtonDraftPool buttonDraftPool, StateToolCardDraft stateToolCardDraft);
    public Boolean checkUsability(ButtonDraftPool buttonDraftPool, StateWindowEnd stateWindowEnd);
    public Boolean checkUsability(ButtonDraftPool buttonDraftPool, StateWindowPlace stateWindowPlace);
    public Boolean checkUsability(ButtonDraftPool buttonDraftPool, StateWindowStart stateWindowStart);
    public Boolean checkUsability(ButtonDraftPool buttonDraftPool, StateRoundTracker stateRoundTracker);
    public Boolean checkUsability(ButtonDraftPool buttonDraftPool, StateTurn stateTurn);

    public Boolean checkUsability(ButtonToolCard buttonToolCard, StateToolCardDraft stateToolCardDraft);
    public Boolean checkUsability(ButtonToolCard buttonToolCard, StateWindowEnd stateWindowEnd);
    public Boolean checkUsability(ButtonToolCard buttonToolCard, StateWindowPlace stateWindowPlace);
    public Boolean checkUsability(ButtonToolCard buttonToolCard, StateWindowStart stateWindowStart);
    public Boolean checkUsability(ButtonToolCard buttonToolCard, StateRoundTracker stateRoundTracker);
    public Boolean checkUsability(ButtonToolCard buttonToolCard, StateTurn stateTurn);

    Boolean checkUsability(ButtonDraftPool buttonDraftPool, State currentState);

    Boolean checkUsability(ButtonPass buttonPass, State currentState);

    Boolean checkUsability(ButtonSquare buttonSquare, State currentState);

    Boolean checkUsability(ButtonToolCard buttonToolCard, State currentState);
}
