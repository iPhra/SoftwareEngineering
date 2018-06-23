package it.polimi.se2018.mvc.controller.toolcardcontroller;

import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.utils.exceptions.ToolCardException;

public abstract class ToolCardController {
    final Board board;
    static final String INVALID_POSITION = "The selected position is invalid";
    static final String PLAYER = "Player ";
    static final String FROM = " from ";
    public ToolCardController(Board board) {
        this.board = board;
    }

    boolean nearPosition (Coordinate firstPosition, Coordinate secondPosition) {
        return firstPosition.getCol() == secondPosition.getCol() &&
                (firstPosition.getRow() == secondPosition.getRow() + 1 ||
                        firstPosition.getRow() == secondPosition.getRow() - 1) ||
                (firstPosition.getRow() == secondPosition.getRow() &&
                        (firstPosition.getCol() == secondPosition.getCol() + 1 ||
                                firstPosition.getCol() == secondPosition.getCol() - 1));
    }

    void updateToolCard(ToolCardMessage toolCardMessage) {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        player.setFavorPoints(player.getFavorPoints()-(board.getToolCardsUsage()[toolCardMessage.getToolCardNumber()]? 2:1));
        board.setAlreadyUsed(toolCardMessage.getToolCardNumber());
        player.setHasUsedCard(true);
        player.dropCardInUse();
    }

    void checkEmptiness(Square squareStart) throws ToolCardException {
        if(squareStart.isEmpty()) throw new ToolCardException("You haven't selected a die\n");
    }

    void revertSquare(Square squareStart, Die dieToMove) throws ToolCardException {
        squareStart.setDie(dieToMove);
        throw new ToolCardException(INVALID_POSITION);
    }

    boolean checkFavorPoints(boolean isUsed, Player player) {
        return (player.getFavorPoints() > 1) || (!isUsed && player.getFavorPoints() == 1);
    }
}
