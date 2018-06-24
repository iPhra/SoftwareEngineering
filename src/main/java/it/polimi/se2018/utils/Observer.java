package it.polimi.se2018.utils;

/**
 * Parametric redefinition of Observer class
 * @param <T> is the class to be observed
 */
public interface Observer<T> {

    void update(T message);

}
