package it.polimi.se2018.mvc.controller;

import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;
import it.polimi.se2018.utils.exceptions.ToolCardException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ModelView implements Serializable{
    private final List<String> playerNames;
    private final List<Integer> playerID;
    private final List<Square[][]> playerWindow;
    private final List<Integer> playerFavorPoint;
    private final List<Die> draftPool;
    private final List<List<Die>> roundTracker;
    private final List<Boolean> usedToolCard;
    private final int round;
    private final List<Boolean> toolCardUsability;
    private final int stateID;
    private int currentPlayerID;
    private boolean hasDieInHand;
    private boolean hasDraftedDie;
    private boolean hasUsedCard;
    private int cardInUse;
    private Die dieInHand;

    public ModelView(Board board) {
        this.usedToolCard = new ArrayList<>();
        playerWindow = new ArrayList<>();
        playerFavorPoint = new ArrayList<>();
        playerNames = new ArrayList<>();
        playerID = new ArrayList<>();
        toolCardUsability = new ArrayList<>();
        round = board.getRound().getRoundNumber();
        draftPool = board.getDraftPool().modelViewCopy();
        roundTracker = board.getRoundTracker().modelViewCopy();
        stateID = board.getStateID();
        setToolCards(board);
        setCurrentPlayer(board);
        setPlayers(board);
    }

    private void setToolCards(Board board) {
        for (boolean b : board.getToolCardsUsage()) {
            this.usedToolCard.add(b);
        }
    }

    private void setPlayers(Board board) {
        for (Player player : board.getPlayers()) {
            playerWindow.add(player.getWindow().modelViewCopy());
            playerFavorPoint.add(player.getFavorPoints());
            playerNames.add(player.getName());
            playerID.add(player.getId());
        }
    }

    private void setCurrentPlayer(Board board) {
        Player currentPlayer = board.getPlayerByID(board.getRound().getCurrentPlayerIndex());
        currentPlayerID = currentPlayer.getId();
        hasDieInHand = currentPlayer.hasDieInHand();
        hasDraftedDie = currentPlayer.hasDraftedDie();
        hasUsedCard = currentPlayer.hasUsedCard();
        try {
            cardInUse = currentPlayer.getCardInUse();
        } catch (ToolCardException e) {
            cardInUse = -1;
        }
        if (hasDieInHand) dieInHand = board.getPlayerByID(currentPlayerID).getDieInHand();
        for (int i = 0; i < board.getToolCards().length; i++) {
            ToolCard toolCard = board.getToolCards()[i];
            toolCardUsability.add(toolCard.handleCheck(new ToolCardChecker(board), board.getToolCardsUsage()[i], currentPlayer));
        }
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

    public List<Square[][]> getPlayerWindow() {
        return playerWindow;
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

    public int getCardInUse() {
        return cardInUse;
    }
}