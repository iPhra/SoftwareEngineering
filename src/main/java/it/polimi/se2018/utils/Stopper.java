package it.polimi.se2018.utils;

/**
 * This interface is implemented by a class that can be stopped by a {@link WaitingThread}
 */
public interface Stopper {

    /**
     * This method stops the thread
     * @param message is the message to provide as a reason to be stopped
     */
    void halt(String message);
}