package it.polimi.se2018.view.cli;

import it.polimi.se2018.model.toolcards.*;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;

public interface ToolCardPlayerInputHandler {
    ToolCardMessage getPlayerRequests(CopperFoilBurnisher toolCard, int toolcardnumber);

    ToolCardMessage getPlayerRequests(CorkBackedStraightedge toolCard, int toolcardnumber);

    ToolCardMessage getPlayerRequests(EglomiseBrush toolCard, int toolcardnumber);

    ToolCardMessage getPlayerRequests(FluxBrush toolCard, int toolcardnumber);

    ToolCardMessage getPlayerRequests(FluxRemover toolCard, int toolcardnumber);

    ToolCardMessage getPlayerRequests(GlazingHammer toolCard, int toolcardnumber);

    ToolCardMessage getPlayerRequests(GrindingStone toolCard, int toolcardnumber);

    ToolCardMessage getPlayerRequests(GrozingPliers toolCard, int toolcardnumber);

    ToolCardMessage getPlayerRequests(Lathekin toolCard, int toolcardnumber);

    ToolCardMessage getPlayerRequests(LensCutter toolCard, int toolcardnumber);

    ToolCardMessage getPlayerRequests(RunningPliers toolCard, int toolcardnumber);

    ToolCardMessage getPlayerRequests(TapWheel toolCard, int toolcardnumber);
}
