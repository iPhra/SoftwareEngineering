package it.polimi.se2018.model;

import it.polimi.se2018.model.toolcards.*;

public interface ToolCardCheckerHandler {
    Boolean checkUsability(CopperFoilBurnisher toolCard, boolean isUsed, Player player, Board board);

    Boolean checkUsability(CorkBackedStraightedge toolCard, boolean isUsed, Player player, Board board);

    Boolean checkUsability(EglomiseBrush toolCard, boolean isUsed, Player player, Board board);

    Boolean checkUsability(FluxBrush toolCard, boolean isUsed, Player player, Board board);

    Boolean checkUsability(FluxRemover toolCard, boolean isUsed, Player player, Board board);

    Boolean checkUsability(GlazingHammer toolCard, boolean isUsed, Player player, Board board);

    Boolean checkUsability(GrindingStone toolCard, boolean isUsed, Player player, Board board);

    Boolean checkUsability(GrozingPliers toolCard, boolean isUsed, Player player, Board board);

    Boolean checkUsability(Lathekin toolCard, boolean isUsed, Player player, Board board);

    Boolean checkUsability(LensCutter toolCard, boolean isUsed, Player player, Board board);

    Boolean checkUsability(RunningPliers toolCard, boolean isUsed, Player player, Board board);

    Boolean checkUsability(TapWheel toolCard, boolean isUsed, Player player, Board board);
}
