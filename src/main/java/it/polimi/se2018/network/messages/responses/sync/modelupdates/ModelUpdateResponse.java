package it.polimi.se2018.network.messages.responses.sync.modelupdates;

import it.polimi.se2018.mvc.controller.ToolCardChecker;
import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;
import it.polimi.se2018.network.messages.responses.sync.SyncResponse;
import it.polimi.se2018.network.messages.responses.sync.SyncResponseHandler;

import java.util.ArrayList;
import java.util.List;

public class ModelUpdateResponse extends SyncResponse {
    private final int stateID;
    private final int currentPlayerID;
    private final boolean hasDieInHand;
    private final boolean hasDraftedDie;
    private final boolean hasUsedCard;
    private final int favorPoints;
    private final List<Boolean> toolCardUsage;
    private final List<Boolean> toolCardUsability;
    private Die dieInHand;

    public ModelUpdateResponse(int playerID, int stateID, Board board) {
        super(playerID);
        this.stateID = stateID;
        toolCardUsability = new ArrayList<>();
        toolCardUsage = new ArrayList<>();
        currentPlayerID = board.getRound().getCurrentPlayerID();
        Player currentPlayer = board.getPlayerByID(currentPlayerID);
        favorPoints = board.getPlayerByID(currentPlayerID).getFavorPoints();
        hasDieInHand = currentPlayer.hasDieInHand();
        hasDraftedDie = currentPlayer.hasDraftedDie();
        hasUsedCard = currentPlayer.hasUsedCard();
        if (hasDieInHand) dieInHand = currentPlayer.getDieInHand();
        for (int i = 0; i < board.getToolCards().length; i++) {
            ToolCard toolCard = board.getToolCards()[i];
            toolCardUsage.add(board.getToolCardsUsage()[i]);
            toolCardUsability.add(toolCard.handleCheck(new ToolCardChecker(board), board.getToolCardsUsage()[i], currentPlayer));
        }
    }

    public int getCurrentPlayerID() {
        return currentPlayerID;
    }

    public int getFavorPoints() {
        return favorPoints;
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

    public Die getDieInHand() {
        return dieInHand;
    }

    public List<Boolean> getToolCardUsability() {
        return toolCardUsability;
    }

    public List<Boolean> getToolCardUsage() {
        return toolCardUsage;
    }

    public int getStateID() {
        return stateID;
    }

    @Override
    public void handle(SyncResponseHandler syncResponseHandler) {
        syncResponseHandler.handleResponse(this);
    }
}
