package it.polimi.se2018.Model.Objectives.PublicObjectives;

import it.polimi.se2018.Model.Player;

public class DifferentColors extends PublicObjective { //checks if there are rows/cols with no colors repeated
    private final boolean isRow;
    private final int multiplier;

    public DifferentColors(String imagePath, String title, boolean isRow, int multiplier) {
        super(imagePath, title);
        this.isRow=isRow;
        this.multiplier=multiplier;
    }

    @Override
    public int evalPoints(Player player) {
        return 0;
    }
}
