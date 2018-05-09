package it.polimi.se2018.Network.Messages.Requests;

import it.polimi.se2018.Network.Messages.Coordinate;
import it.polimi.se2018.Model.Player;


import java.util.ArrayList;
import java.util.List;

public class ToolCardMessage extends Message {
    private final int toolCardNumber; //0 to 2, number of toolcard in the board
    private int value;
    private ArrayList<Coordinate> startingPosition;
    private ArrayList<Coordinate> finalPosition;
    private ArrayList draftPoolPosition;
    private ArrayList roundTrackerPosition; //x is the Turn, y is the position

    public ToolCardMessage(Player player, int toolCardNumber, int value) {
        super(player);
        this.toolCardNumber = toolCardNumber;
        this.value = value;
        startingPosition = new ArrayList<>();
        finalPosition = new ArrayList<>();
        draftPoolPosition = new ArrayList<>();
        roundTrackerPosition = new ArrayList<>();
    }

    public int getToolCardNumber() {
        return toolCardNumber;
    }

    public List<Coordinate> getStartingPosition() {
        return startingPosition;
    }

    public List<Coordinate> getFinalPosition() {
        return finalPosition;
    }

    public List<Integer> getDraftPoolPosition() {
        return draftPoolPosition;
    }

    public List<Coordinate> getRoundTrackerPosition() {
        return roundTrackerPosition;
    }

    public void addStartingPosition(Coordinate coordinate) {
        startingPosition.add(coordinate);
    }

    public void addFinalPosition(Coordinate coordinate) {
        finalPosition.add(coordinate);
    }

    public void addDraftPoolPosition(int coordinate) {
        draftPoolPosition.add(coordinate);
    }

    public void addRoundTrackerPosition(Coordinate coordinate) {
        roundTrackerPosition.add(coordinate);
    }

    public int getValue() {
        return value;
    }

    public void addValue(int value) {
        this.value = value;
    }

    @Override
    public void handle(MessageHandler handler) {
        handler.performMove(this);
    }
}
