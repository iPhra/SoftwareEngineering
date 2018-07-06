package it.polimi.se2018.utils.exceptions;

/**
 * This exception is thrown when a problem arises when using a {@link it.polimi.se2018.mvc.model.toolcards.ToolCard}
 */
public class ToolCardException extends Exception {

    public ToolCardException (String message) {
        super(message);
    }
}
