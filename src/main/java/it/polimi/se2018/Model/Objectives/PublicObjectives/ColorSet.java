package it.polimi.se2018.Model.Objectives.PublicObjectives;

import it.polimi.se2018.Model.Player;

public class ColorSet extends PublicObjective { //counts sets of different colors
    private final int multiplier;

    public ColorSet(String imagePath, String title, int multiplier) {
        super(imagePath, title);
        this.multiplier=multiplier;
    }

    @Override
    public int evalPoints(Player player) { //da sovrascrivere ancora
        return 0;
    }
}
