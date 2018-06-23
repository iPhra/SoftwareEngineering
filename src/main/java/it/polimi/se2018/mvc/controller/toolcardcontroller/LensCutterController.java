package it.polimi.se2018.mvc.controller.toolcardcontroller;

import it.polimi.se2018.mvc.model.Board;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;

public class LensCutterController extends ToolCardController {
    public LensCutterController(Board board) {
        super(board);
    }

    public boolean checkUsability(boolean isUsed, Player player) {
        boolean condition = checkFavorPoints(isUsed, player);
        if(!player.hasDieInHand()) condition = false;
        //you need to check if a die in the round tracker exists
        if(board.getRoundTracker().isVoid()) condition = false;
        return condition;
    }

    public void useCard(ToolCardMessage toolCardMessage) {
        Player player = board.getPlayerByID(toolCardMessage.getPlayerID());
        Die dieDrafted = player.getDieInHand();
        Die dieFromRoundTrack = board.getRoundTracker().getDie(toolCardMessage.getRoundTrackerPosition().getRow(), toolCardMessage.getRoundTrackerPosition().getCol());
        player.setDieInHand(dieFromRoundTrack);
        board.getRoundTracker().addToRoundTracker(toolCardMessage.getRoundTrackerPosition().getRow(), dieDrafted);
        updateToolCard(toolCardMessage);
        board.createRoundTrackerResponse(PLAYER + player.getName() + " used Lens Cutter: \nhw/she swapped the drafted die with the die " + dieFromRoundTrack.getValue() + " " + dieFromRoundTrack.getColor() + " from the Round Tracker\n");
    }
}
