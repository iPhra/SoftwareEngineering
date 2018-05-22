package it.polimi.se2018.client.view.cli;

import it.polimi.se2018.model.toolcards.*;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.utils.exceptions.TimeOutException;

public class ToolCardPlayerInput implements ToolCardPlayerInputHandler {
    private int playerID;
    private CLIInput cliInput;

    ToolCardPlayerInput (int playerID, CLIInput cliInput) {
        this.playerID=playerID;
        this.cliInput=cliInput;

    }

    @Override
    public ToolCardMessage getPlayerRequests(CopperFoilBurnisher toolCard, int toolcardnumber) throws TimeOutException {
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, toolcardnumber);
        Coordinate startingCoordinate = cliInput.getDieInMap();
        if (startingCoordinate.equals(new Coordinate(-1, -1))) {
            toolCardMessage.setToDismiss(true);
            return toolCardMessage;
        }
        Coordinate finalPosition = cliInput.getCoordinate();
        if (startingCoordinate.equals(new Coordinate(-1, -1))) {
            toolCardMessage.setToDismiss(true);
            return toolCardMessage;
        }
        toolCardMessage.addStartingPosition(startingCoordinate);
        toolCardMessage.addFinalPosition(finalPosition);
        return  toolCardMessage;
    }

    @Override
    public ToolCardMessage getPlayerRequests(CorkBackedStraightedge toolCard, int toolcardnumber) throws TimeOutException{
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, toolcardnumber);
        Coordinate finalPosition = cliInput.getCoordinate();
        if (finalPosition.equals(new Coordinate(-1, -1))) {
            toolCardMessage.setToDismiss(true);
            return toolCardMessage;
        }
        toolCardMessage.addFinalPosition(finalPosition);
        return  toolCardMessage;
    }

    @Override
    public ToolCardMessage getPlayerRequests(EglomiseBrush toolCard, int toolcardnumber) throws TimeOutException{
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, toolcardnumber);
        Coordinate startingCoordinate = cliInput.getDieInMap();
        if (startingCoordinate.equals(new Coordinate(-1, -1))) {
            toolCardMessage.setToDismiss(true);
            return toolCardMessage;
        }
        Coordinate finalPosition = cliInput.getCoordinate();
        if (finalPosition.equals(new Coordinate(-1, -1))) {
            toolCardMessage.setToDismiss(true);
            return toolCardMessage;
        }
        toolCardMessage.addStartingPosition(startingCoordinate);
        toolCardMessage.addFinalPosition(finalPosition);
        return  toolCardMessage;
    }

    @Override
    public ToolCardMessage getPlayerRequests(FluxBrush toolCard, int toolcardnumber) throws TimeOutException{
        return new ToolCardMessage(playerID, toolcardnumber);
    }

    @Override
    public ToolCardMessage getPlayerRequests(FluxRemover toolCard, int toolcardnumber) throws TimeOutException{
        return new ToolCardMessage(playerID, toolcardnumber);
    }

    @Override
    public ToolCardMessage getPlayerRequests(GlazingHammer toolCard, int toolcardnumber) throws TimeOutException{
        return new ToolCardMessage(playerID, toolcardnumber);
    }

    @Override
    public ToolCardMessage getPlayerRequests(GrindingStone toolCard, int toolcardnumber) throws TimeOutException{
        return new ToolCardMessage(playerID, toolcardnumber);
    }

    @Override
    public ToolCardMessage getPlayerRequests(GrozingPliers toolCard, int toolcardnumber) throws TimeOutException{
        int choice = cliInput.getMinusPlus();
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, toolcardnumber);
        toolCardMessage.addValue(choice);
        return  toolCardMessage;
    }

    @Override
    public ToolCardMessage getPlayerRequests(Lathekin toolCard, int toolcardnumber) throws TimeOutException{
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, toolcardnumber);
        Coordinate startingCoordinateOne = cliInput.getDieInMap();
        if (startingCoordinateOne.equals(new Coordinate(-1, -1))) {
            toolCardMessage.setToDismiss(true);
            return toolCardMessage;
        }
        Coordinate finalPositionOne = cliInput.getCoordinate();
        if (finalPositionOne.equals(new Coordinate(-1, -1))) {
            toolCardMessage.setToDismiss(true);
            return toolCardMessage;
        }
        toolCardMessage.addStartingPosition(startingCoordinateOne);
        toolCardMessage.addFinalPosition(finalPositionOne);
        Coordinate startingCoordinateTwo = cliInput.getDieInMap();
        if (startingCoordinateTwo.equals(new Coordinate(-1, -1))) {
            toolCardMessage.setToDismiss(true);
            return toolCardMessage;
        }
        Coordinate finalPositionTwo = cliInput.getCoordinate();
        if (finalPositionTwo.equals(new Coordinate(-1, -1))) {
            toolCardMessage.setToDismiss(true);
            return toolCardMessage;
        }
        toolCardMessage.addStartingPosition(startingCoordinateTwo);
        toolCardMessage.addFinalPosition(finalPositionTwo);
        return  toolCardMessage;
    }

    @Override
    public ToolCardMessage getPlayerRequests(LensCutter toolCard, int toolcardnumber) throws TimeOutException{
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, toolcardnumber);
        Coordinate positionRoundTrack = cliInput.getRoundTrackPosition();
        if (positionRoundTrack.getRow() == -1) toolCardMessage.setToDismiss(true);
        toolCardMessage.addRoundTrackerPosition(positionRoundTrack);
        return  toolCardMessage;
    }

    @Override
    public ToolCardMessage getPlayerRequests(RunningPliers toolCard, int toolcardnumber) throws TimeOutException{
        return new ToolCardMessage(playerID, toolcardnumber);
    }

    @Override
    public ToolCardMessage getPlayerRequests(TapWheel toolCard, int toolcardnumber) throws TimeOutException{
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, toolcardnumber);
        Coordinate startingCoordinateOne = cliInput.getDieInMap();
        if (startingCoordinateOne.equals(new Coordinate(-1, -1))) {
            toolCardMessage.setToDismiss(true);
            return toolCardMessage;
        }
        Coordinate finalPositionOne = cliInput.getCoordinate();
        if (finalPositionOne.equals(new Coordinate(-1, -1))) {
            toolCardMessage.setToDismiss(true);
            return toolCardMessage;
        }
        toolCardMessage.addStartingPosition(startingCoordinateOne);
        toolCardMessage.addFinalPosition(finalPositionOne);
        Coordinate startingCoordinateTwo = cliInput.getDieInMap();
        if (startingCoordinateTwo.equals(new Coordinate(-1, -1))) {
            toolCardMessage.setToDismiss(true);
            return toolCardMessage;
        }
        Coordinate finalPositionTwo = cliInput.getCoordinate();
        if (finalPositionTwo.equals(new Coordinate(-1, -1))) {
            toolCardMessage.setToDismiss(true);
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
