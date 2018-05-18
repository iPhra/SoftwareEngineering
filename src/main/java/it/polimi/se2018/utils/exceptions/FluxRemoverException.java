package it.polimi.se2018.utils.exceptions;

import it.polimi.se2018.model.Color;

public class FluxRemoverException extends  Exception{
    Color color;

    public FluxRemoverException (Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
