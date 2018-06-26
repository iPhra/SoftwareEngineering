package it.polimi.se2018.client.view.cli;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InputGetter implements Runnable {
    private final CLIView cliView;
    private final BufferedReader bufferedReader;
    private final PrintStream printStream;
    private final int top;
    private final int bottom;

    InputGetter(CLIView cliView, BufferedReader bufferedReader, PrintStream printStream, int top, int bottom) {
        this.cliView = cliView;
        this.bufferedReader = bufferedReader;
        this.printStream = printStream;
        this.top = top;
        this.bottom = bottom;
    }

    @Override
    public synchronized void run() {
        boolean condition = true;
        int res = 0;
        try {
            int junk = System.in.read(new byte[System.in.available()]);
        }
        catch (IOException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL, e.getMessage());
        }
        do {
            try {
                while(!bufferedReader.ready()) {
                    wait(200);
                }
                res = Integer.parseInt(bufferedReader.readLine());
                if (res > top || res < bottom) throw new NumberFormatException();
                condition = false;
            }
            catch(IOException | NumberFormatException e) {
                printStream.println("Input is invalid\n");
            }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                cliView.setInterrupted();
                return;
            }
        }
        while(condition);
        cliView.giveInput(res);
    }
}
