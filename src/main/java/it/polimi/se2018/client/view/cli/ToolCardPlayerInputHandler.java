package it.polimi.se2018.client.view.cli;

import it.polimi.se2018.mvc.model.toolcards.*;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.utils.exceptions.ChangeActionException;
import it.polimi.se2018.utils.exceptions.HaltException;

public interface ToolCardPlayerInputHandler {

    ToolCardMessage getPlayerRequests(CopperFoilBurnisher toolCard, int toolCardNumber) throws HaltException, ChangeActionException;
    ToolCardMessage getPlayerRequests(CorkBackedStraightedge toolCard, int toolCardNumber) throws HaltException, ChangeActionException;
    ToolCardMessage getPlayerRequests(EglomiseBrush toolCard, int toolCardNumber) throws HaltException, ChangeActionException;
    ToolCardMessage getPlayerRequests(FluxBrush toolCard, int toolCardNumber);
    ToolCardMessage getPlayerRequests(FluxRemover toolCard, int toolCardNumber);
    ToolCardMessage getPlayerRequests(GlazingHammer toolCard, int toolCardNumber);
    ToolCardMessage getPlayerRequests(GrindingStone toolCard, int toolCardNumber);
    ToolCardMessage getPlayerRequests(GrozingPliers toolCard, int toolCardNumber) throws HaltException;
    ToolCardMessage getPlayerRequests(Lathekin toolCard, int toolCardNumber) throws HaltException, ChangeActionException;
    ToolCardMessage getPlayerRequests(LensCutter toolCard, int toolCardNumber) throws HaltException, ChangeActionException;
    ToolCardMessage getPlayerRequests(RunningPliers toolCard, int toolCardNumber);
    ToolCardMessage getPlayerRequests(TapWheel toolCard, int toolCardNumber) throws HaltException, ChangeActionException;
}
