package it.polimi.se2018.model;

import it.polimi.se2018.utils.exceptions.ToolCardException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ModelView implements Serializable{
    private final List<String> playerName;
    private final List<Integer> playerID;
    private final List<Square[][]> playerMap;
    private final List<Integer> playerFavorPoint;
    private final List<Die> draftPool;
    private final List<List<Die>> roundTracker;
    private final List<Boolean> usedToolCard;
    private final int turn;

    private final boolean hasDieInHand;
    private final boolean hasDraftedDie;
    private  final boolean hasUsedCard;
    private int cardInUse;

    public ModelView(Board board) {
        this.usedToolCard = new ArrayList<>();
        playerMap = new ArrayList<>();
        playerFavorPoint = new ArrayList<>();
        playerName = new ArrayList<>();
        playerID = new ArrayList<>();
        for (boolean b : board.getToolCardsUsage()) {
            this.usedToolCard.add(b);
        }
        int index;
        Player currentPlayer = board.getPlayerByIndex(board.getRound().getCurrentPlayerIndex());
        for (Player player : board.getPlayers()) {
            playerMap.add(player.getWindow().modelViewCopy());
            playerFavorPoint.add(player.getFavorPoints());
            playerName.add(player.getName());
            playerID.add(player.getId());
            if (board.getRound().getCurrentPlayerIndex() == player.getId()) {
                index = player.getId();
                currentPlayer = board.getPlayerByIndex(index);
            }
        }
        hasDieInHand = currentPlayer.hasDieInHand();
        hasDraftedDie = currentPlayer.hasDraftedDie();
        hasUsedCard = currentPlayer.hasUsedCard();
        try {
            cardInUse = currentPlayer.getCardInUse();
        } catch (ToolCardException e) {
            cardInUse = -1;
        }
        turn = board.getRoundTracker().getTurn();
        draftPool = board.getDraftPool().modelViewCopy();
        roundTracker = board.getRoundTracker().modelViewCopy();
    }


    public List<String> getPlayerName() {
        return playerName;
    }

    public List<Integer> getPlayerID() {
        return playerID;
    }

    public List<Square[][]> getPlayerMap() {
        return playerMap;
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

    public List<Boolean> getUsedToolCards() {
        return usedToolCard;
    }

    public int getTurn() {
        return turn;
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

    public int getCardInUse() {
        return cardInUse;
    }

}