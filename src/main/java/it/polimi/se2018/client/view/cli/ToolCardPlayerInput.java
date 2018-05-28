package it.polimi.se2018.client.view.cli;

import it.polimi.se2018.mvc.model.toolcards.*;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.utils.exceptions.HaltException;

public class ToolCardPlayerInput implements ToolCardPlayerInputHandler {
    private final int playerID;
    private final CLIInput cliInput;

    ToolCardPlayerInput (int playerID, CLIInput cliInput) {
        this.playerID=playerID;
        this.cliInput=cliInput;
    }

    private boolean checkPosition(Coordinate coordinate, ToolCardMessage toolCardMessage) {
        if (coordinate.getRow()==-1 && coordinate.getCol()==-1) {
            toolCardMessage.setToDismiss(true);
            return true;
        }
        return false;
    }

    private ToolCardMessage getToolCardMessage(int toolCardNumber) throws HaltException {
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, cliInput.getBoard().getStateID(),toolCardNumber);
        Coordinate startingCoordinate = cliInput.getCoordinate();
        if (checkPosition(startingCoordinate,toolCardMessage)) {
            return toolCardMessage;
        }
        Coordinate finalPosition = cliInput.getCoordinate();
        if (checkPosition(finalPosition,toolCardMessage)) {
            return toolCardMessage;
        }
        toolCardMessage.addStartingPosition(startingCoordinate);
        toolCardMessage.addFinalPosition(finalPosition);
        return  toolCardMessage;
    }

    @Override
    public ToolCardMessage getPlayerRequests(CopperFoilBurnisher toolCard, int toolcardnumber) throws HaltException {
        return getToolCardMessage(toolcardnumber);
    }

    @Override
    public ToolCardMessage getPlayerRequests(EglomiseBrush toolCard, int toolcardnumber) throws HaltException {
        return getToolCardMessage(toolcardnumber);
    }

    @Override
    public ToolCardMessage getPlayerRequests(CorkBackedStraightedge toolCard, int toolcardnumber) throws HaltException {
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, cliInput.getBoard().getStateID(),toolcardnumber);
        Coordinate finalPosition = cliInput.getCoordinate();
        if (checkPosition(finalPosition,toolCardMessage)) {
            return toolCardMessage;
        }
        toolCardMessage.addFinalPosition(finalPosition);
        return  toolCardMessage;
    }

    @Override
    public ToolCardMessage getPlayerRequests(FluxBrush toolCard, int toolcardnumber) throws HaltException {
        return new ToolCardMessage(playerID, cliInput.getBoard().getStateID(),toolcardnumber);
    }

    @Override
    public ToolCardMessage getPlayerRequests(FluxRemover toolCard, int toolcardnumber) throws HaltException {
        return new ToolCardMessage(playerID, cliInput.getBoard().getStateID(),toolcardnumber);
    }

    @Override
    public ToolCardMessage getPlayerRequests(GlazingHammer toolCard, int toolcardnumber) throws HaltException {
        return new ToolCardMessage(playerID, cliInput.getBoard().getStateID(),toolcardnumber);
    }

    @Override
    public ToolCardMessage getPlayerRequests(GrindingStone toolCard, int toolcardnumber) throws HaltException {
        return new ToolCardMessage(playerID, cliInput.getBoard().getStateID(),toolcardnumber);
    }

    @Override
    public ToolCardMessage getPlayerRequests(GrozingPliers toolCard, int toolcardnumber) throws HaltException {
        int choice = cliInput.getIncrementOrDecrement();
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, cliInput.getBoard().getStateID(),toolcardnumber);
        toolCardMessage.addValue(choice);
        return  toolCardMessage;
    }

    @Override
    public ToolCardMessage getPlayerRequests(Lathekin toolCard, int toolcardnumber) throws HaltException {
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, cliInput.getBoard().getStateID(),toolcardnumber);
        Coordinate startingCoordinateOne = cliInput.getCoordinate();
        if ((checkPosition(startingCoordinateOne,toolCardMessage))) {
            return toolCardMessage;
        }
        Coordinate finalPositionOne = cliInput.getCoordinate();
        if (checkPosition(finalPositionOne,toolCardMessage)) {
            return toolCardMessage;
        }
        toolCardMessage.addStartingPosition(startingCoordinateOne);
        toolCardMessage.addFinalPosition(finalPositionOne);
        Coordinate startingCoordinateTwo = cliInput.getCoordinate();
        if ((checkPosition(startingCoordinateTwo,toolCardMessage))) {
            return toolCardMessage;
        }
        Coordinate finalPositionTwo = cliInput.getCoordinate();
        if (checkPosition(finalPositionTwo,toolCardMessage)) {
            return toolCardMessage;
        }
        toolCardMessage.addStartingPosition(startingCoordinateTwo);
        toolCardMessage.addFinalPosition(finalPositionTwo);
        return  toolCardMessage;
    }

    @Override
    public ToolCardMessage getPlayerRequests(LensCutter toolCard, int toolcardnumber) throws HaltException {
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID,cliInput.getBoard().getStateID(), toolcardnumber);
        Coordinate positionRoundTrack = cliInput.getRoundTrackPosition();
        if (positionRoundTrack.getRow() == -1) toolCardMessage.setToDismiss(true);
        toolCardMessage.addRoundTrackerPosition(positionRoundTrack);
        return  toolCardMessage;
    }

    @Override
    public ToolCardMessage getPlayerRequests(RunningPliers toolCard, int toolcardnumber) throws HaltException {
        return new ToolCardMessage(playerID, cliInput.getBoard().getStateID(),toolcardnumber);
    }

    @Override
    public ToolCardMessage getPlayerRequests(TapWheel toolCard, int toolcardnumber) throws HaltException {
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, cliInput.getBoard().getStateID(),toolcardnumber);
        Coordinate startingCoordinateOne = cliInput.getCoordinate();
        if ((checkPosition(startingCoordinateOne,toolCardMessage))) {
            return toolCardMessage;
        }
        Coordinate finalPositionOne = cliInput.getCoordinate();
        if (checkPosition(finalPositionOne,toolCardMessage)) {
            return toolCardMessage;
        }
        toolCardMessage.addStartingPosition(startingCoordinateOne);
        toolCardMessage.addFinalPosition(finalPositionOne);
        Coordinate startingCoordinateTwo = cliInput.getCoordinate();
        if (checkPosition(startingCoordinateTwo,toolCardMessage)) {
            return toolCardMessage;
        }
        Coordinate finalPositionTwo = cliInput.getCoordinate();
        if ((checkPosition(finalPositionTwo,toolCardMessage))) {
            return toolCardMessage;
        }
        Coordinate positionRoundTrack = cliInput.getRoundTrackPosition();
        if (positionRoundTrack.getRow() == -1) {
            toolCardMessage.setToDismiss(true);
            return toolCardMessage;
        }
        toolCardMessage.addStartingPosition(startingCoordinateTwo);
        toolCardMessage.addFinalPosition(finalPositionTwo);
        toolCardMessage.addRoundTrackerPosition(positionRoundTrack);
        return  toolCardMessage;
    }
}
