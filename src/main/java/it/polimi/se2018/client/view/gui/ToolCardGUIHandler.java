package it.polimi.se2018.client.view.gui;

import it.polimi.se2018.mvc.model.toolcards.*;

public interface ToolCardGUIHandler   {

    void getPlayerRequests(CopperFoilBurnisher toolCard, int toolCardNumber);
    void getPlayerRequests(CorkBackedStraightedge toolCard, int toolCardNumber);
    void getPlayerRequests(EglomiseBrush toolCard, int toolCardNumber);
    void getPlayerRequests(FluxBrush toolCard, int toolCardNumber);
    void getPlayerRequests(FluxRemover toolCard, int toolCardNumber);
    void getPlayerRequests(GlazingHammer toolCard, int toolCardNumber);
    void getPlayerRequests(GrindingStone toolCard, int toolCardNumber);
    void getPlayerRequests(GrozingPliers toolCard, int toolCardNumber);
    void getPlayerRequests(Lathekin toolCard, int toolCardNumber);
    void getPlayerRequests(LensCutter toolCard, int toolCardNumber);
    void getPlayerRequests(RunningPliers toolCard, int toolCardNumber);
    void getPlayerRequests(TapWheel toolCard, int toolCardNumber);
}