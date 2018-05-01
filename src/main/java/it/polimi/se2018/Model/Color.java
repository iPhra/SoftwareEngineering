package it.polimi.se2018.Model;

public enum Color {
    BLUE,RED,GREEN,YELLOW,PURPLE,WHITE;

    private Color() {
    }

    public static int fromColor (Color color) {
        int index = 0;
        for (Color col : Color.values()) {
            if (col == color) break;
            index += 1;
        }
        return index;
    }

    public static Color fromInt(int value) throws Exception{
        Color color = null;
        switch (value) {
            case 0: color = Color.BLUE; break;
            case 1: color = Color.RED; break;
            case 2: color = Color.GREEN; break;
            case 3: color = Color.YELLOW; break;
            case 4: color = Color.PURPLE; break;
            case 5: color = Color.WHITE; break;
            default: throw new Exception("Invalid value");
        }
        return color;
    }
}
