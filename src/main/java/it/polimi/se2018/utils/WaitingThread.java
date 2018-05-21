package it.polimi.se2018.utils;

import java.time.Duration;

public class WaitingThread extends Thread {

    public Duration timeout;
    private Timing caller;
    private Boolean stopped = false;

    /**
     * Constructs a waiting thread with a given timeout and caller
     *
     * @param timeout
     *            the timeout that you want to assign to the waiting thread
     * @param caller
     *            the caller that you want to assign to the waiting thread
     */
    public WaitingThread(Duration timeout, Timing caller) {
        this.timeout = timeout;
        this.caller = caller;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    /**
     * this method must be ran when you want to start the timeout
     */
    public void run() {
        try {
            Thread.sleep(timeout.toMillis());
        } catch (InterruptedException e) {
            stopped = true;
        }
        if (!stopped) {
            System.out.println("waiting thread: time's up");
            caller.onTimesUp();
        }

    }

}