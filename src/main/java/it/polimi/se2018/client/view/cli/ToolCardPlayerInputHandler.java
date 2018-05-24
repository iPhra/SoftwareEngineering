package it.polimi.se2018.client.view.cli;

import it.polimi.se2018.mvc.model.toolcards.*;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.utils.exceptions.TimeoutException;

public interface ToolCardPlayerInputHandler {

    ToolCardMessage getPlayerRequests(CopperFoilBurnisher toolCard, int toolCardNumber) throws TimeoutException;
    ToolCardMessage getPlayerRequests(CorkBackedStraightedge toolCard, int toolCardNumber) throws TimeoutException;
    ToolCardMessage getPlayerRequests(EglomiseBrush toolCard, int toolCardNumber) throws TimeoutException;
    ToolCardMessage getPlayerRequests(FluxBrush toolCard, int toolCardNumber) throws TimeoutException;
    ToolCardMessage getPlayerRequests(FluxRemover toolCard, int toolCardNumber) throws TimeoutException;
    ToolCardMessage getPlayerRequests(GlazingHammer toolCard, int toolCardNumber) throws TimeoutException;
    ToolCardMessage getPlayerRequests(GrindingStone toolCard, int toolCardNumber) throws TimeoutException;
    ToolCardMessage getPlayerRequests(GrozingPliers toolCard, int toolCardNumber) throws TimeoutException;
    ToolCardMessage getPlayerRequests(Lathekin toolCard, int toolCardNumber) throws TimeoutException;
    ToolCardMessage getPlayerRequests(LensCutter toolCard, int toolCardNumber) throws TimeoutException;
    ToolCardMessage getPlayerRequests(RunningPliers toolCard, int toolCardNumber) throws TimeoutException;
    ToolCardMessage getPlayerRequests(TapWheel toolCard, int toolCardNumber) throws TimeoutException;
}
