package it.polimi.se2018.Model;

/**
 * Enumeration for all the colors of {@link Die} and {@link Square}
 * @Author Francesco Lorenzo
 */
public enum Color {
    BLUE,RED,GREEN,YELLOW,PURPLE,WHITE;

    private Color() {
    }

    /**
     * Given a color returns its index as if the enumeration was an array
     * @param color is the given color
     * @return the index of the given color
     */
    public static int fromColor (Color color) {
        int index = 0;
        for (Color col : Color.values()) {
            if (col == color) return index;
            index += 1;
        }
        return -1;
    }

}
