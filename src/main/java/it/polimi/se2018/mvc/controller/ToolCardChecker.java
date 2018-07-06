package it.polimi.se2018.mvc.controller;

import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.mvc.model.toolcards.*;

public class ToolCardChecker implements ToolCardCheckerHandler {
    private final Board board;

    public ToolCardChecker(Board board) {
        this.board = board;
    }

    private boolean checkFavorPoints(boolean isUsed, Player player) {
        return (player.getFavorPoints() > 1) || (!isUsed && player.getFavorPoints() == 1);
    }

    @Override
    public boolean checkUsability(CopperFoilBurnisher toolCard, boolean isUsed, Player player) {
        boolean condition = checkFavorPoints(isUsed, player);
        //you need to check if 1 die exists: if so, there are 18 empty slots or less
        if(player.getWindow().countEmptySlots()>18) condition = false;
        return condition;
    }

    @Override
    public boolean checkUsability(CorkBackedStraightedge toolCard, boolean isUsed, Player player) {
        boolean condition = checkFavorPoints(isUsed, player);
        if(!player.hasDieInHand()) condition = false;
        return condition;
    }

    @Override
    public boolean checkUsability(EglomiseBrush toolCard, boolean isUsed, Player player) {
        boolean condition = checkFavorPoints(isUsed, player);
        //you need to check if 1 die exists: if so, there are 18 empty slots or less
        if(player.getWindow().countEmptySlots()>18) condition = false;
        return condition;
    }

    @Override
    public boolean checkUsability(FluxBrush toolCard, boolean isUsed, Player player) {
        boolean condition = checkFavorPoints(isUsed, player);
        if(!player.hasDieInHand()) condition = false;
        return condition;
    }

    @Override
    public boolean checkUsability(FluxRemover toolCard, boolean isUsed, Player player) {
        boolean condition = checkFavorPoints(isUsed, player);
        if(!player.hasDieInHand()) condition = false;
        return condition;
    }

    @Override
    public boolean checkUsability(GlazingHammer toolCard, boolean isUsed, Player player) {
        boolean condition = checkFavorPoints(isUsed, player);
        if(board.getRound().isFirstRotation()) condition = false;
        if(player.hasDraftedDie()) condition = false;
        return condition;
    }

    @Override
    public boolean checkUsability(GrindingStone toolCard, boolean isUsed, Player player) {
        boolean condition = checkFavorPoints(isUsed, player);
        if(!player.hasDieInHand()) condition = false;
        return condition;
    }

    @Override
    public boolean checkUsability(GrozingPliers toolCard, boolean isUsed, Player player) {
        boolean condition = checkFavorPoints(isUsed, player);
        if(!player.hasDieInHand()) condition = false;
        return condition;
    }

    @Override
    public boolean checkUsability(Lathekin toolCard, boolean isUsed, Player player) {
        boolean condition = checkFavorPoints(isUsed, player);
        //because you need to check if 3 dice exist: if yes, there are 18 empty slots or less
        if (player.getWindow().countEmptySlots() > 17) condition = false;
        return condition;
    }

    @Override
    public boolean checkUsability(LensCutter toolCard, boolean isUsed, Player player) {
        boolean condition = checkFavorPoints(isUsed, player);
        if(!player.hasDieInHand()) condition = false;
        //you need to check if a die in the round tracker exists
        if(board.getRoundTracker().isVoid()) condition = false;
        return condition;
    }

    @Override
    public boolean checkUsability(RunningPliers toolCard, boolean isUsed, Player player) {
        boolean condition = checkFavorPoints(isUsed, player);
        if(!board.getRound().isFirstRotation()) condition = false;
        if(!player.hasDraftedDie() || (player.hasDraftedDie() && player.hasDieInHand())) condition = false;
        return condition;
    }

    @Override
    public boolean checkUsability(TapWheel toolCard, boolean isUsed, Player player) {
        //you need to check if two dice exist: if so there are 18 empty slots or less left
        if(!checkFavorPoints(isUsed, player) || player.getWindow().countEmptySlots() > 18) return false;
        for(Square square: player.getWindow()) {
            if(!square.isEmpty() && board.getRoundTracker().containsColor(square.getDie().getColor())) return true;
        }
        return false;
    }
}