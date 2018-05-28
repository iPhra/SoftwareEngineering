package it.polimi.se2018.utils;

import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WaitingThread extends Thread {

    private Duration timeout;
    private final Stopper caller;
    private Boolean stopped = false;

    public WaitingThread(Duration timeout, Stopper caller) {
        this.timeout = timeout;
        this.caller = caller;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(timeout.toMillis());
        } catch (InterruptedException e) {
            stopped = true;
            Thread.currentThread().interrupt();
        }
        if (!stopped) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,"Time is up");
            caller.halt("Time is up");
        }
    }

}