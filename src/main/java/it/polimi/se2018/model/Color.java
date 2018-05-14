package it.polimi.se2018.model;

import java.io.Serializable;

/**
 * Enumeration for all the colors of {@link Die} and {@link Square}
 * @author Francesco Lorenzo
 */
public enum Color implements Serializable {
    BLUE,RED,GREEN,YELLOW,PURPLE,WHITE;

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

    /**
     * Given an abbreviation of the color returns the actual color corresponding to that abbreviation
     * @param string is the abbreviation
     * @return a color
     */
    public static Color fromAbbreviation(String string) {
        switch (string) {
            case "W" : return Color.WHITE;
            case "B" : return Color.BLUE;
            case "R" : return Color.RED;
            case "G" : return Color.GREEN;
            case "Y" : return Color.YELLOW;
            case "P" : return Color.PURPLE;
            default : return null;
        }
    }

}
