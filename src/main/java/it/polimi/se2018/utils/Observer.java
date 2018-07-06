package it.polimi.se2018.utils;

/**
 * Parametric redefinition of Observer class
 * @param <T> is the class to be observed
 */
public interface Observer<T> {

    /**
     * This method is called when you want to notify the observer
     * @param message is the message to notify
     */
    void update(T message);
}
