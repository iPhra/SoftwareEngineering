package it.polimi.se2018.mvc.controller;

import it.polimi.se2018.mvc.model.toolcards.*;
import it.polimi.se2018.network.messages.responses.Response;
import it.polimi.se2018.utils.exceptions.ToolCardException;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;

public interface ToolCardHandler {

    Response useCard(CopperFoilBurnisher toolCard, ToolCardMessage toolCardMessage) throws ToolCardException;
    Response useCard(CorkBackedStraightedge toolCard, ToolCardMessage toolCardMessage) throws ToolCardException;
    Response useCard(EglomiseBrush toolCard, ToolCardMessage toolCardMessage) throws ToolCardException;
    Response useCard(FluxBrush toolCard, ToolCardMessage toolCardMessage) throws ToolCardException;
    Response useCard(FluxRemover toolCard, ToolCardMessage toolCardMessage) throws ToolCardException;
    Response useCard(GlazingHammer toolCard, ToolCardMessage toolCardMessage) throws ToolCardException;
    Response useCard(GrindingStone toolCard, ToolCardMessage toolCardMessage) throws ToolCardException;
    Response useCard(GrozingPliers toolCard, ToolCardMessage toolCardMessage) throws ToolCardException;
    Response useCard(Lathekin toolCard, ToolCardMessage toolCardMessage) throws ToolCardException;
    Response useCard(LensCutter toolCard, ToolCardMessage toolCardMessage) throws ToolCardException;
    Response useCard(RunningPliers toolCard, ToolCardMessage toolCardMessage) throws ToolCardException;
    Response useCard(TapWheel toolCard, ToolCardMessage toolCardMessage) throws ToolCardException;
}
