package it.polimi.se2018.model;

import it.polimi.se2018.model.toolcards.*;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;

public interface ToolCardCheckerHandler {
    Boolean checkUsability(CopperFoilBurnisher toolCard, boolean isUsed, Player player, boolean isFirstTurn);

    Boolean checkUsability(CorkBackedStraightedge toolCard, boolean isUsed, Player player, boolean isFirstTurn);

    Boolean checkUsability(EglomiseBrush toolCard, boolean isUsed, Player player, boolean isFirstTurn);

    Boolean checkUsability(FluxBrush toolCard, boolean isUsed, Player player, boolean isFirstTurn);

    Boolean checkUsability(FluxRemover toolCard, boolean isUsed, Player player, boolean isFirstTurn);

    Boolean checkUsability(GlazingHammer toolCard, boolean isUsed, Player player, boolean isFirstTurn);

    Boolean checkUsability(GrindingStone toolCard, boolean isUsed, Player player, boolean isFirstTurn);

    Boolean checkUsability(GrozingPliers toolCard, boolean isUsed, Player player, boolean isFirstTurn);

    Boolean checkUsability(Lathekin toolCard, boolean isUsed, Player player, boolean isFirstTurn);

    Boolean checkUsability(LensCutter toolCard, boolean isUsed, Player player, boolean isFirstTurn);

    Boolean checkUsability(RunningPliers toolCard, boolean isUsed, Player player, boolean isFirstTurn);

    Boolean checkUsability(TapWheel toolCard, boolean isUsed, Player player, boolean isFirstTurn);
}
