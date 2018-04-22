package it.polimi.se2018.Model;

public class ColouredObjective extends PrivateObjective {
    private Color color;

    public ColouredObjective(String imagePath, String title, Color color) {
        super(imagePath, title);
        this.color=color;
    }

    public void evalPoints() {}

    public Color getColor() {
        return color;
    }
}
