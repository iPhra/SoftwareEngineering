package it.polimi.se2018.network.messages.responses.sync.modelupdates;

import it.polimi.se2018.mvc.controller.ToolCardChecker;
import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;

import java.io.Serializable;
import java.util.*;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class ModelView implements Serializable {
    private final List<String> playerNames;
    private final List<Integer> playerIDs;
    private final List<Square[][]> playerWindows;
    private final List<Integer> playerFavorPoints;
    private List<Die> draftPool;
    private List<List<Die>> roundTracker;
    private List<Boolean> toolCardUsability;
    private List<Boolean> toolCardUsage;
    private int stateID;
    private int currentPlayerID;
    private boolean hasDieInHand;
    private boolean hasDraftedDie;
    private boolean hasUsedCard;
    private Die dieInHand;

    public ModelView(Board board) {
        playerWindows = new ArrayList<>();
        playerFavorPoints = new ArrayList<>();
        playerNames = new ArrayList<>();
        playerIDs = new ArrayList<>();
        toolCardUsability = new ArrayList<>();
        toolCardUsage = new ArrayList<>();
        draftPool = board.getDraftPool().modelViewCopy();
        roundTracker = board.getRoundTracker().modelViewCopy();
        stateID = board.getStateID();
        setCurrentPlayer(board);
        setPlayers(board);
    }

    private void setPlayers(Board board) {
        for (Player player : board.getPlayers()) {
            playerWindows.add(player.getWindow().modelViewCopy());
            playerFavorPoints.add(player.getFavorPoints());
            playerNames.add(player.getName());
            playerIDs.add(player.getId());
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
            toolCardUsage.add(board.getToolCardsUsage()[i]);
            toolCardUsability.add(toolCard.handleCheck(new ToolCardChecker(board), board.getToolCardsUsage()[i], currentPlayer));
        }
    }

    public void setPlayerWindow(int playerID, Square[][] playerWindow) {
        playerWindows.set(playerID,playerWindow);
    }

    public void setPlayerFavorPoint(int playerID, int favorPoints) {
        playerFavorPoints.set(playerID,favorPoints);
    }

    public void setDraftPool(List<Die> draftPool) {
        this.draftPool = draftPool;
    }

    public void setRoundTracker(List<List<Die>> roundTracker) {
        this.roundTracker = roundTracker;
    }

    public void setToolCardUsability(List<Boolean> toolCardUsability) {
        this.toolCardUsability = toolCardUsability;
    }

    public void setToolCardUsage(List<Boolean> toolCardUsage) {
        this.toolCardUsage = toolCardUsage;
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

    public List<Boolean> getToolCardUsage() {
        return toolCardUsage;
    }

    public Die getDieInHand() { return dieInHand; }

    public int getCurrentPlayerID() {
        return currentPlayerID;
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public List<Integer> getPlayerIDs() {
        return playerIDs;
    }

    public List<Square[][]> getPlayerWindows() {
        return playerWindows;
    }

    public List<Integer> getPlayerFavorPoints() {
        return playerFavorPoints;
    }

    public List<Die> getDraftPool() {
        return draftPool;
    }

    public List<List<Die>> getRoundTracker() {
        return roundTracker;
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