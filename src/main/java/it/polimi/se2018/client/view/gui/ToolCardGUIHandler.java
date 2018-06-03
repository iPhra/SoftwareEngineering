package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.mvc.model.toolcards.*;
import it.polimi.se2018.utils.exceptions.ChangeActionException;
import it.polimi.se2018.utils.exceptions.HaltException;

public interface ToolCardGUIHandler   {

    void getPlayerRequests(CopperFoilBurnisher toolCard, int toolCardNumber) throws HaltException, ChangeActionException;
    void getPlayerRequests(CorkBackedStraightedge toolCard, int toolCardNumber) throws HaltException, ChangeActionException;
    void getPlayerRequests(EglomiseBrush toolCard, int toolCardNumber) throws HaltException, ChangeActionException;
    void getPlayerRequests(FluxBrush toolCard, int toolCardNumber);
    void getPlayerRequests(FluxRemover toolCard, int toolCardNumber);
    void getPlayerRequests(GlazingHammer toolCard, int toolCardNumber);
    void getPlayerRequests(GrindingStone toolCard, int toolCardNumber);
    void getPlayerRequests(GrozingPliers toolCard, int toolCardNumber) throws HaltException;
    void getPlayerRequests(Lathekin toolCard, int toolCardNumber) throws HaltException, ChangeActionException;
    void getPlayerRequests(LensCutter toolCard, int toolCardNumber) throws HaltException, ChangeActionException;
    void getPlayerRequests(RunningPliers toolCard, int toolCardNumber);
    void getPlayerRequests(TapWheel toolCard, int toolCardNumber) throws HaltException, ChangeActionException;
}