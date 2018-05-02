package it.polimi.se2018.Model.Moves;

import it.polimi.se2018.Model.Player;


import java.util.ArrayList;
import java.util.List;

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
}
