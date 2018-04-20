package it.polimi.se2018.Model;

public enum Color {
    BLUE("B"),RED("R"),GREEN("G"),YELLOW("Y"),PURPLE("P"),WHITE("W");
    private String abbreviation;

    private Color(String abbreviation) {
    }

    public String getAbbreviation(){return abbreviation;}
}
