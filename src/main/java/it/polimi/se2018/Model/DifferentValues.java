package it.polimi.se2018.Model;

public class DifferentValues extends PublicObjective {
    private final int multiplier;
    private final boolean isRow;

    public DifferentValues(String imagePath, String title, boolean isRow, int multiplier) {
        super(imagePath, title);
        this.isRow=isRow;
        this.multiplier=multiplier;
    }

    @Override
    public int evalPoints(Player player) {
        return 0;
    }
}
