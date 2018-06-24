package it.polimi.se2018.utils;

/**
 * This interface is implemented by a class that can be stopped by a {@link WaitingThread}
 */
public interface Stopper {

    void halt(String message);
}