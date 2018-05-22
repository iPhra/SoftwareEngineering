package it.polimi.se2018.controller;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.toolcards.*;

public interface ToolCardCheckerHandler {

    boolean checkUsability(CopperFoilBurnisher toolCard, boolean isUsed, Player player);
    boolean checkUsability(CorkBackedStraightedge toolCard, boolean isUsed, Player player);
    boolean checkUsability(EglomiseBrush toolCard, boolean isUsed, Player player);
    boolean checkUsability(FluxBrush toolCard, boolean isUsed, Player player);
    boolean checkUsability(FluxRemover toolCard, boolean isUsed, Player player);
    boolean checkUsability(GlazingHammer toolCard, boolean isUsed, Player player);
    boolean checkUsability(GrindingStone toolCard, boolean isUsed, Player player);
    boolean checkUsability(GrozingPliers toolCard, boolean isUsed, Player player);
    boolean checkUsability(Lathekin toolCard, boolean isUsed, Player player);
    boolean checkUsability(LensCutter toolCard, boolean isUsed, Player player);
    boolean checkUsability(RunningPliers toolCard, boolean isUsed, Player player);
    boolean checkUsability(TapWheel toolCard, boolean isUsed, Player player);
}