package it.polimi.se2018.utils;

import java.time.Duration;

public class WaitingThread extends Thread {

    private Duration timeout;
    private final Timing caller;
    private Boolean stopped = false;

    public WaitingThread(Duration timeout, Timing caller) {
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
        }
        if (!stopped) {
            caller.wakeUp();
        }
    }

}