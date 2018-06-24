package it.polimi.se2018.utils;

import java.time.Duration;

/**
 * This class is used to implement timers
 */
public class WaitingThread extends Thread {

    /**
     * This is the duration of the timer
     */
    private final Duration timeout;

    /**
     * This is the thread running this thread
     */
    private final Stopper caller;

    /**
     * {@code true} if this thread was interrupted before waking up
     */
    private Boolean stopped = false;

    public WaitingThread(Duration timeout, Stopper caller) {
        this.timeout = timeout;
        this.caller = caller;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(timeout.toMillis());
        }
        catch (InterruptedException e) {
            stopped = true;
            Thread.currentThread().interrupt();
        }
        if (!stopped) { //if no one interrupted this thread, then the timer has expired and i have to interrupt the caller
            caller.halt("Time is up");
        }
    }
}