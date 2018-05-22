package it.polimi.se2018.controller;

import it.polimi.se2018.model.Board;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.toolcards.*;

public class ToolCardChecker implements ToolCardCheckerHandler {
    private Board board;

    public ToolCardChecker(Board board) {
        this.board = board;
    }

    private boolean checkUsage(boolean isUsed, Player player) {
        return (isUsed && player.getFavorPoints() == 1) || (player.getFavorPoints() < 1);
    }

    public boolean checkUsability(CopperFoilBurnisher toolCard, boolean isUsed, Player player) {
        boolean condition = true;
        condition = checkUsage(isUsed, player);
        //you need to check if 1 die exists: if so, there are 19 empty slots or less
        if(player.getWindow().countEmptySlots()>19) condition = false;
        return condition;
    }

    public boolean checkUsability(CorkBackedStraightedge toolCard, boolean isUsed, Player player) {
        boolean condition = true;
        condition = checkUsage(isUsed, player);
        if(!player.hasDieInHand()) condition = false;
        return condition;
    }

    public boolean checkUsability(EglomiseBrush toolCard, boolean isUsed, Player player) {
        boolean condition = true;
        condition = checkUsage(isUsed, player);
        //you need to check if 1 die exists: if so, there are 19 empty slots or less
        if(player.getWindow().countEmptySlots()>19) condition = false;
        return condition;
    }

    public boolean checkUsability(FluxBrush toolCard, boolean isUsed, Player player) {
        boolean condition = true;
        condition = checkUsage(isUsed, player);
        if(!player.hasDieInHand()) condition = false;
        return condition;
    }

    public boolean checkUsability(FluxRemover toolCard, boolean isUsed, Player player) {
        boolean condition = true;
        condition = checkUsage(isUsed, player);
        if(!player.hasDieInHand()) condition = false;
        return condition;
    }

    public boolean checkUsability(GlazingHammer toolCard, boolean isUsed, Player player) {
        boolean condition = true;
        condition = checkUsage(isUsed, player);
        if(board.getRound().isFirstRotation()) condition = false;
        if(player.hasDraftedDie()) condition = false;
        return condition;
    }

    public boolean checkUsability(GrindingStone toolCard, boolean isUsed, Player player) {
        boolean condition = true;
        condition = checkUsage(isUsed, player);
        if(!player.hasDieInHand()) condition = false;
        return condition;
    }

    public boolean checkUsability(GrozingPliers toolCard, boolean isUsed, Player player) {
        boolean condition = true;
        condition = checkUsage(isUsed, player);
        if(!player.hasDieInHand()) condition = false;
        return condition;
    }

    public boolean checkUsability(Lathekin toolCard, boolean isUsed, Player player) {
        boolean condition = true;
        condition = checkUsage(isUsed, player);
        //because you need to check if 2 dice exist: if yes, there are 18 empty slots or less
        if (player.getWindow().countEmptySlots() > 18) condition = false;
        return condition;
    }

    public boolean checkUsability(LensCutter toolCard, boolean isUsed, Player player) {
        boolean condition = true;
        condition = checkUsage(isUsed, player);
        if(!player.hasDieInHand()) condition = false;
        //you need to check if a die in the round tracker exists
        if(board.getRoundTracker().isVoid()) condition = false;
        return condition;
    }

    public boolean checkUsability(RunningPliers toolCard, boolean isUsed, Player player) {
        boolean condition = true;
        condition = checkUsage(isUsed, player);
        if(!board.getRound().isFirstRotation()) condition = false;
        if(!player.hasDraftedDie() || (player.hasDraftedDie() && player.hasDieInHand())) condition = false;
        return condition;
    }

    public boolean checkUsability(TapWheel toolCard, boolean isUsed, Player player) {
        boolean condition = true;
        condition = checkUsage(isUsed, player);
        //you need to check if two dice exist: if so there are 18 empty slots or less left
        if(player.getWindow().countEmptySlots()>18) condition = false;
        //you need to check if a die on the round tracker exists
        if (board.getRoundTracker().isVoid()) condition = false;
        return condition;
    }
}