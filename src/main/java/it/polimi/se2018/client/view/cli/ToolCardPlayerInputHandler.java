package it.polimi.se2018.client.view.cli;

import it.polimi.se2018.model.toolcards.*;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.utils.exceptions.TimeOutException;

public interface ToolCardPlayerInputHandler {

    ToolCardMessage getPlayerRequests(CopperFoilBurnisher toolCard, int toolCardNumber) throws TimeOutException;

    ToolCardMessage getPlayerRequests(CorkBackedStraightedge toolCard, int toolCardNumber) throws TimeOutException;

    ToolCardMessage getPlayerRequests(EglomiseBrush toolCard, int toolCardNumber) throws TimeOutException;

    ToolCardMessage getPlayerRequests(FluxBrush toolCard, int toolCardNumber) throws TimeOutException;

    ToolCardMessage getPlayerRequests(FluxRemover toolCard, int toolCardNumber) throws TimeOutException;

    ToolCardMessage getPlayerRequests(GlazingHammer toolCard, int toolCardNumber) throws TimeOutException;

    ToolCardMessage getPlayerRequests(GrindingStone toolCard, int toolCardNumber) throws TimeOutException;

    ToolCardMessage getPlayerRequests(GrozingPliers toolCard, int toolCardNumber) throws TimeOutException;

    ToolCardMessage getPlayerRequests(Lathekin toolCard, int toolCardNumber) throws TimeOutException;

    ToolCardMessage getPlayerRequests(LensCutter toolCard, int toolCardNumber) throws TimeOutException;

    ToolCardMessage getPlayerRequests(RunningPliers toolCard, int toolCardNumber) throws TimeOutException;

    ToolCardMessage getPlayerRequests(TapWheel toolCard, int toolCardNumber) throws TimeOutException;
}
