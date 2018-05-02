package it.polimi.se2018.Model.Moves;

import it.polimi.se2018.Model.Player;

import java.util.ArrayList;

public class MoveMessage {
    private final Player player;
    private final int idMove;
    private ArrayList<Coordinate> startingPosition;
    private ArrayList<Coordinate> finalPosition;
    private ArrayList<Integer> draftPoolPosition;
    private ArrayList<Coordinate> roundTrackerPosition;

    public MoveMessage(Player player, int idMove) {
        this.player = player;
        this.idMove = idMove;
        startingPosition = new ArrayList<>();
        finalPosition = new ArrayList<>();
        draftPoolPosition = new ArrayList<>();
        roundTrackerPosition = new ArrayList<>();
    }

    public Player getPlayer () { return player; }

    public int getIdMove() {
        return idMove;
    }

    public ArrayList<Coordinate> getStartingPosition() {
        return startingPosition;
    }

    public ArrayList<Coordinate> getFinalPosition() {
        return finalPosition;
    }

    public ArrayList<Integer> getDraftPoolPosition() {
        return draftPoolPosition;
    }

    public ArrayList<Coordinate> getRoundTrackerPosition() {
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
}
