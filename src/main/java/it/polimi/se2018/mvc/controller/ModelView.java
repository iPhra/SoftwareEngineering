package it.polimi.se2018.mvc.controller;

import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ModelView implements Serializable{
    private List<String> playerNames;
    private List<Integer> playerID;
    private List<Square[][]> playerWindows;
    private List<Integer> playerFavorPoint;
    private List<Die> draftPool;
    private List<List<Die>> roundTracker;
    private int round;
    private List<Boolean> toolCardUsability;
    private int stateID;
    private int currentPlayerID;
    private boolean hasDieInHand;
    private boolean hasDraftedDie;
    private boolean hasUsedCard;
    private Die dieInHand;

    public ModelView(Board board) {
        playerWindows = new ArrayList<>();
        playerFavorPoint = new ArrayList<>();
        playerNames = new ArrayList<>();
        playerID = new ArrayList<>();
        toolCardUsability = new ArrayList<>();
        round = board.getRound().getRoundNumber();
        draftPool = board.getDraftPool().modelViewCopy();
        roundTracker = board.getRoundTracker().modelViewCopy();
        stateID = board.getStateID();
        setCurrentPlayer(board);
        setPlayers(board);
    }

    private void setPlayers(Board board) {
        for (Player player : board.getPlayers()) {
            playerWindows.add(player.getWindow().modelViewCopy());
            playerFavorPoint.add(player.getFavorPoints());
            playerNames.add(player.getName());
            playerID.add(player.getId());
        }
    }

    private void setCurrentPlayer(Board board) {
        Player currentPlayer = board.getPlayerByID(board.getRound().getCurrentPlayerID());
        currentPlayerID = currentPlayer.getId();
        hasDieInHand = currentPlayer.hasDieInHand();
        hasDraftedDie = currentPlayer.hasDraftedDie();
        hasUsedCard = currentPlayer.hasUsedCard();
        if (hasDieInHand) dieInHand = board.getPlayerByID(currentPlayerID).getDieInHand();
        for (int i = 0; i < board.getToolCards().length; i++) {
            ToolCard toolCard = board.getToolCards()[i];
            toolCardUsability.add(toolCard.handleCheck(new ToolCardChecker(board), board.getToolCardsUsage()[i], currentPlayer));
        }
    }

    public void setPlayerNames(List<String> playerNames) {
        this.playerNames = playerNames;
    }

    public void setPlayerID(List<Integer> playerID) {
        this.playerID = playerID;
    }

    public void setPlayerWindow(int playerID, Square[][] playerWindow) {
        playerWindows.set(playerID,playerWindow);
    }

    public void setPlayerFavorPoint(int playerID, int favorPoints) {
        playerFavorPoint.set(playerID,favorPoints);
    }

    public void setDraftPool(List<Die> draftPool) {
        this.draftPool = draftPool;
    }

    public void setRoundTracker(List<List<Die>> roundTracker) {
        this.roundTracker = roundTracker;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void setToolCardUsability(List<Boolean> toolCardUsability) {
        this.toolCardUsability = toolCardUsability;
    }

    public void setStateID(int stateID) {
        this.stateID = stateID;
    }

    public void setCurrentPlayerID(int currentPlayerID) {
        this.currentPlayerID = currentPlayerID;
    }

    public void setHasDieInHand(boolean hasDieInHand) {
        this.hasDieInHand = hasDieInHand;
    }

    public void setHasDraftedDie(boolean hasDraftedDie) {
        this.hasDraftedDie = hasDraftedDie;
    }

    public void setHasUsedCard(boolean hasUsedCard) {
        this.hasUsedCard = hasUsedCard;
    }

    public void setDieInHand(Die dieInHand) {
        this.dieInHand = dieInHand;
    }

    public int getStateID() {return stateID;}

    public List<Boolean> getToolCardUsability() {
        return toolCardUsability;
    }

    public Die getDieInHand() { return dieInHand; }

    public int getCurrentPlayerID() {
        return currentPlayerID;
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public List<Integer> getPlayerID() {
        return playerID;
    }

    public List<Square[][]> getPlayerWindows() {
        return playerWindows;
    }

    public List<Integer> getPlayerFavorPoint() {
        return playerFavorPoint;
    }

    public List<Die> getDraftPool() {
        return draftPool;
    }

    public List<List<Die>> getRoundTracker() {
        return roundTracker;
    }

    public int getRound() {
        return round;
    }

    public boolean hasDieInHand() {
        return hasDieInHand;
    }

    public boolean hasDraftedDie() {
        return hasDraftedDie;
    }

    public boolean hasUsedCard() {
        return hasUsedCard;
    }
}