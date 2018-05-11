package it.polimi.se2018.model;

/**
 * Enumeration for all the colors of {@link Die} and {@link Square}
 * @author Francesco Lorenzo
 */
public enum Color {
    BLUE,RED,GREEN,YELLOW,PURPLE,WHITE;

    Color() {
    }

    /**
     * Given a color returns its index as if the enumeration was an array
     * An exception is not necessary in this method, returning a value outside of the right domain is enough for our use of this class
     * @param color is the given color
     * @return the index of the given color if it exists, {@code -1} otherwise
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
