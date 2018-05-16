package it.polimi.se2018.model.toolcards;

import it.polimi.se2018.controller.ToolCardHandler;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.utils.exceptions.ToolCardException;
import it.polimi.se2018.network.messages.requests.ToolCardMessage;
import it.polimi.se2018.view.cli.CLIClientView;


public class Lathekin extends ToolCard {

    public Lathekin(String imagePath) {
        super(imagePath, "Lathekin", "Move exactly two dice, obeying all placement restrictions");
    }

    @Override
    public void handle(ToolCardHandler handler, ToolCardMessage message) throws ToolCardException{
        handler.useCard(this, message);
    }
    public ToolCardMessage getPlayerRequests(int playerID, CLIClientView clientView, int toolcardnumber) {
        Coordinate startingCoordinateOne = clientView.getDieInMap();
        Coordinate finalPositionOne = clientView.getCoordinate();
        ToolCardMessage toolCardMessage = new ToolCardMessage(playerID, toolcardnumber);
        toolCardMessage.addStartingPosition(startingCoordinateOne);
        toolCardMessage.addFinalPosition(finalPositionOne);
        Coordinate startingCoordinateTwo = clientView.getDieInMap();
        Coordinate finalPositionTwo = clientView.getCoordinate();
        toolCardMessage.addStartingPosition(startingCoordinateTwo);
        toolCardMessage.addFinalPosition(finalPositionTwo);
        return  toolCardMessage;
    }
}