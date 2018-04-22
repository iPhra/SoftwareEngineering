package it.polimi.se2018.Model;

public class ColoredDiagonals extends PublicObjective { //+1 for each die of the same color in a diagonal

    public ColoredDiagonals(String imagePath, String title) {
        super(imagePath, title);
    }

    @Override
    public int evalPoints(Player player) {
        return 0;
    }
}

