package it.polimi.se2018.Model.Objectives.PublicObjectives;

import it.polimi.se2018.Model.Player;

public class ValueSet extends PublicObjective { //checks how many sets of "values" are in the map, everywhere
    private final int multiplier;
    private final int[] values;

    public ValueSet(String imagePath, String title, int multiplier, int[] values) {
        super(imagePath, title);
        this.multiplier=multiplier;
        this.values=values;
    }

    @Override
    public int evalPoints(Player player) {
        return 0;
    }
}
