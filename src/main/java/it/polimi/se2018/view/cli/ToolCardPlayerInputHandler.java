package it.polimi.se2018.view.cli;

import it.polimi.se2018.model.toolcards.*;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;

public interface ToolCardPlayerInputHandler {
    public ToolCardMessage getPlayerRequests(CopperFoilBurnisher toolCard, int toolcardnumber);
    public ToolCardMessage getPlayerRequests(CorkBackedStraightedge toolCard, int toolcardnumber);
    public ToolCardMessage getPlayerRequests(EglomiseBrush toolCard, int toolcardnumber);
    public ToolCardMessage getPlayerRequests(FluxBrush toolCard, int toolcardnumber);
    public ToolCardMessage getPlayerRequests(FluxRemover toolCard, int toolcardnumber);
    public ToolCardMessage getPlayerRequests(GlazingHammer toolCard, int toolcardnumber);
    public ToolCardMessage getPlayerRequests(GrindingStone toolCard, int toolcardnumber);
    public ToolCardMessage getPlayerRequests(GrozingPliers toolCard, int toolcardnumber);
    public ToolCardMessage getPlayerRequests(Lathekin toolCard, int toolcardnumber);
    public ToolCardMessage getPlayerRequests(LensCutter toolCard, int toolcardnumber);
    public ToolCardMessage getPlayerRequests(RunningPliers toolCard, int toolcardnumber);
    public ToolCardMessage getPlayerRequests(TapWheel toolCard, int toolcardnumber);
}
