package it.polimi.se2018.view.cli;

import it.polimi.se2018.model.toolcards.*;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;

public interface ToolCardPlayerInputHandler {

    ToolCardMessage getPlayerRequests(CopperFoilBurnisher toolCard, int toolCardNumber);

    ToolCardMessage getPlayerRequests(CorkBackedStraightedge toolCard, int toolCardNumber);

    ToolCardMessage getPlayerRequests(EglomiseBrush toolCard, int toolCardNumber);

    ToolCardMessage getPlayerRequests(FluxBrush toolCard, int toolCardNumber);

    ToolCardMessage getPlayerRequests(FluxRemover toolCard, int toolCardNumber);

    ToolCardMessage getPlayerRequests(GlazingHammer toolCard, int toolCardNumber);

    ToolCardMessage getPlayerRequests(GrindingStone toolCard, int toolCardNumber);

    ToolCardMessage getPlayerRequests(GrozingPliers toolCard, int toolCardNumber);

    ToolCardMessage getPlayerRequests(Lathekin toolCard, int toolCardNumber);

    ToolCardMessage getPlayerRequests(LensCutter toolCard, int toolCardNumber);

    ToolCardMessage getPlayerRequests(RunningPliers toolCard, int toolCardNumber);

    ToolCardMessage getPlayerRequests(TapWheel toolCard, int toolCardNumber);
}
