package it.polimi.se2018.Model;

public enum Color {
    BLUE("B"),RED("R"),GREEN("G"),YELLOW("Y"),PURPLE("P"),WHITE("W");
    private String abbreviation;

    private Color(String abbreviation) {
    }
    public static int fromColor (Color color) {
        int index = 0;
        for (Color col : Color.values()) {
            if (col == color) break;
            index += 1;
        }
        return index;
    }

    public String getAbbreviation(){
        return abbreviation;
    }
    public static Color fromInt(int value) {
        Color color = null;
        switch (value) {
            case 0: color = Color.BLUE; break;
            case 1: color = Color.RED; break;
            case 2: color = Color.GREEN; break;
            case 3: color = Color.YELLOW; break;
            case 4: color = Color.PURPLE; break;
            case 5: color = Color.WHITE; break;
        }
        return color;
    }
}
