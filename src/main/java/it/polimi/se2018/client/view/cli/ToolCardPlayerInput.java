package it.polimi.se2018.client.view.cli;

import it.polimi.se2018.model.toolcards.*;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;

public class ToolCardPlayerInput implements ToolCardPlayerInputHandler {
    private int playerID;
    private CLIInput cliInput;

    ToolCardPlayerInput (int playerID, CLIInput cliInput) {
        this.playerID=playerID;
        this.cliInput=cliInput;

    }

    @Override
    public ToolCardMessage getPlayerRequests(CopperFoilBurnisher toolCard, int toolcardnumber) {
        Coordinate startingCoordinate = cliInput.getDieInMap();
        Coordinate finalPosition = cliInput.getCoordinate();
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, toolcardnumber);
        toolCardMessage.addStartingPosition(startingCoordinate);
        toolCardMessage.addFinalPosition(finalPosition);
        return  toolCardMessage;
    }

    @Override
    public ToolCardMessage getPlayerRequests(CorkBackedStraightedge toolCard, int toolcardnumber) {
        Coordinate finalPosition = cliInput.getCoordinate();
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, toolcardnumber);
        toolCardMessage.addFinalPosition(finalPosition);
        return  toolCardMessage;
    }

    @Override
    public ToolCardMessage getPlayerRequests(EglomiseBrush toolCard, int toolcardnumber) {
        Coordinate startingCoordinate = cliInput.getDieInMap();
        Coordinate finalPosition = cliInput.getCoordinate();
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, toolcardnumber);
        toolCardMessage.addStartingPosition(startingCoordinate);
        toolCardMessage.addFinalPosition(finalPosition);
        return  toolCardMessage;
    }

    @Override
    public ToolCardMessage getPlayerRequests(FluxBrush toolCard, int toolcardnumber) {
        return new ToolCardMessage(playerID, toolcardnumber);
    }

    @Override
    public ToolCardMessage getPlayerRequests(FluxRemover toolCard, int toolcardnumber) {
        return new ToolCardMessage(playerID, toolcardnumber);
    }

    @Override
    public ToolCardMessage getPlayerRequests(GlazingHammer toolCard, int toolcardnumber) {
        return new ToolCardMessage(playerID, toolcardnumber);
    }

    @Override
    public ToolCardMessage getPlayerRequests(GrindingStone toolCard, int toolcardnumber) {
        return new ToolCardMessage(playerID, toolcardnumber);
    }

    @Override
    public ToolCardMessage getPlayerRequests(GrozingPliers toolCard, int toolcardnumber) {
        int choice = cliInput.getMinusPlus();
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, toolcardnumber);
        toolCardMessage.addValue(choice);
        return  toolCardMessage;
    }

    @Override
    public ToolCardMessage getPlayerRequests(Lathekin toolCard, int toolcardnumber) {
        Coordinate startingCoordinateOne = cliInput.getDieInMap();
        Coordinate finalPositionOne = cliInput.getCoordinate();
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, toolcardnumber);
        toolCardMessage.addStartingPosition(startingCoordinateOne);
        toolCardMessage.addFinalPosition(finalPositionOne);
        Coordinate startingCoordinateTwo = cliInput.getDieInMap();
        Coordinate finalPositionTwo = cliInput.getCoordinate();
        toolCardMessage.addStartingPosition(startingCoordinateTwo);
        toolCardMessage.addFinalPosition(finalPositionTwo);
        return  toolCardMessage;
    }

    @Override
    public ToolCardMessage getPlayerRequests(LensCutter toolCard, int toolcardnumber) {
        Coordinate positionRoundTrack = cliInput.getRoundTrackPosition();
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, toolcardnumber);
        toolCardMessage.addRoundTrackerPosition(positionRoundTrack);
        return  toolCardMessage;
    }

    @Override
    public ToolCardMessage getPlayerRequests(RunningPliers toolCard, int toolcardnumber) {
        return new ToolCardMessage(playerID, toolcardnumber);
    }

    @Override
    public ToolCardMessage getPlayerRequests(TapWheel toolCard, int toolcardnumber) {
        Coordinate startingCoordinateOne = cliInput.getDieInMap();
        Coordinate finalPositionOne = cliInput.getCoordinate();
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, toolcardnumber);
        toolCardMessage.addStartingPosition(startingCoordinateOne);
        toolCardMessage.addFinalPosition(finalPositionOne);
        Coordinate startingCoordinateTwo = cliInput.getDieInMap();
        Coordinate finalPositionTwo = cliInput.getCoordinate();
        Coordinate positionRoundTrack = cliInput.getRoundTrackPosition();
        toolCardMessage.addStartingPosition(startingCoordinateTwo);
        toolCardMessage.addFinalPosition(finalPositionTwo);
        toolCardMessage.addRoundTrackerPosition(positionRoundTrack);
        return  toolCardMessage;
    }
}
