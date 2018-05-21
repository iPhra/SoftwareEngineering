package it.polimi.se2018.model;

import it.polimi.se2018.model.toolcards.*;

public class ToolCardChecker implements ToolCardCheckerHandler {
    public Boolean checkUsability(CopperFoilBurnisher toolCard, boolean isUsed, Player player, Board board) {
        boolean condition = true;
        if ((isUsed && player.getFavorPoints() == 1) || player.getFavorPoints() < 1) condition = false;
        return condition;
    }

    public Boolean checkUsability(CorkBackedStraightedge toolCard, boolean isUsed, Player player, Board board) {
        boolean condition = true;
        if ((isUsed && player.getFavorPoints() == 1) || player.getFavorPoints() < 1) condition = false;
        if(!player.hasDieInHand()) condition = false;
        return condition;
    }

    public Boolean checkUsability(EglomiseBrush toolCard, boolean isUsed, Player player, Board board) {
        boolean condition = true;
        if ((isUsed && player.getFavorPoints() == 1) || player.getFavorPoints() < 1) condition = false;
        return condition;
    }

    public Boolean checkUsability(FluxBrush toolCard, boolean isUsed, Player player, Board board) {
        boolean condition = true;
        if ((isUsed && player.getFavorPoints() == 1) || player.getFavorPoints() < 1) condition = false;
        if(!player.hasDieInHand()) condition = false;
        return condition;
    }

    public Boolean checkUsability(FluxRemover toolCard, boolean isUsed, Player player, Board board) {
        boolean condition = true;
        if ((isUsed && player.getFavorPoints() == 1) || player.getFavorPoints() < 1) condition = false;
        if(!player.hasDieInHand()) condition = false;
        return condition;
    }

    public Boolean checkUsability(GlazingHammer toolCard, boolean isUsed, Player player, Board board) {
        boolean condition = true;
        if ((isUsed && player.getFavorPoints() == 1) || player.getFavorPoints() < 1) condition = false;
        if(board.getRound().isFirstRotation()) condition = false;
        if(player.hasDraftedDie()) condition = false;
        return condition;
    }

    public Boolean checkUsability(GrindingStone toolCard, boolean isUsed, Player player, Board board) {
        boolean condition = true;
        if ((isUsed && player.getFavorPoints() == 1) || player.getFavorPoints() < 1) condition = false;
        if(!player.hasDieInHand()) condition = false;
        return condition;
    }

    public Boolean checkUsability(GrozingPliers toolCard, boolean isUsed, Player player, Board board) {
        boolean condition = true;
        if ((isUsed && player.getFavorPoints() == 1) || player.getFavorPoints() < 1) condition = false;
        if(!player.hasDieInHand()) condition = false;
        return condition;
    }

    public Boolean checkUsability(Lathekin toolCard, boolean isUsed, Player player, Board board) {
        boolean condition = true;
        if ((isUsed && player.getFavorPoints() == 1) || player.getFavorPoints() < 1) condition = false;
        //because you need to check if 2 dice exist: if yes, there are 18 empty slots or less
        if (player.getWindow().countEmptySlots() > 18) condition = false;
        return condition;
    }

    public Boolean checkUsability(LensCutter toolCard, boolean isUsed, Player player, Board board) {
        boolean condition = true;
        if ((isUsed && player.getFavorPoints() == 1) || player.getFavorPoints() < 1) condition = false;
        if(!player.hasDieInHand()) condition = false;
        if(board.getRoundTracker().isVoid()) condition = false;
        return condition;
    }

    public Boolean checkUsability(RunningPliers toolCard, boolean isUsed, Player player, Board board) {
        boolean condition = true;
        if ((isUsed && player.getFavorPoints() == 1) || player.getFavorPoints() < 1) condition = false;
        if(!board.getRound().isFirstRotation()) condition = false;
        if(!player.hasDraftedDie() || (player.hasDraftedDie() && player.hasDieInHand())) condition = false;
        return condition;
    }

    public Boolean checkUsability(TapWheel toolCard, boolean isUsed, Player player, Board board) {
        boolean condition = true;
        if ((isUsed && player.getFavorPoints() == 1) || player.getFavorPoints() < 1) condition = false;
        if (board.getRoundTracker().isVoid()) condition = false;
        return condition;
    }
}