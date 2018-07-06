package it.polimi.se2018.client.view.cli;

import it.polimi.se2018.client.Client;
import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.utils.exceptions.ChangeActionException;
import it.polimi.se2018.utils.exceptions.HaltException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

import static java.lang.System.*;

/**
 * This calss is the view client-side. It's usedif you use a CLI
 */
public class CLIView extends ClientView {
    private final CLIData cliModel;
    private final PrintStream printStream;
    private final BufferedReader bufferedReader;
    private Thread inputGetter;
    private boolean inputGiven;
    private boolean stopped;
    private int input;

    public CLIView(Client client, int playerID) {
        super(client);
        cliModel = new CLIData(this,playerID);
        CLILogic cliLogic = new CLILogic(this, cliModel, playerID);
        register(cliLogic);
        printStream = new PrintStream(out);
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Print a text to the player
     * @param string that you show to the player
     */
    void print(String string) { printStream.print(string); }

    /**
     * Take the input from the player with the limitation derived from the future usage of that input
     * @param bottom minimum input that the player can give
     * @param top maximum input that the player can give
     * @return the input from the player
     * @throws HaltException is called if the time is up
     */
    synchronized int takeInput(int bottom, int top) throws HaltException {
        inputGetter = new Thread(new InputGetter(this, bufferedReader,printStream,top,bottom));
        inputGetter.start();
        isIterating = true;
        while(!inputGiven) {
            try {
                this.wait();
            }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        isIterating = false;
        inputGiven = false;
        if(stopped) {
            stopped = false;
            throw new HaltException();
        }
        else return input;
    }

    /**
     * This method is called by InputGiver after a value has been read from the command line
     * @param input is the
     */
    synchronized void giveInput(int input) {
        this.input = input;
        inputGiven = true;
        notifyAll();
    }

    /**
     * This method is called by InputGiver if it was interrupted by a timeout and hasn't read anything from the command line
     */
    synchronized void setInterrupted() {
        stopped = true;
        inputGiven = true;
        notifyAll();
    }

    /**
     * Get a coorinate from the player
     * @return the given coordinate
     * @throws ChangeActionException is the exception that throw if you would change action
     * @throws HaltException is called if the time is up
     */
    Coordinate getCoordinate() throws ChangeActionException, HaltException {
        cliModel.showYourWindow();
        printStream.println("\n\nChoose the row");
        int row = takeInput(0, 3);
        printStream.println("\n\nChoose the column");
        int col = takeInput(0, 4);
        printStream.println("\n\nYou chose the position. Press: \n [1] to accept  [2] to change  [3] to do another action");
        int choice = takeInput(1, 3);
        switch(choice) {
            case 1 : return new Coordinate(row,col);
            case 2 : return getCoordinate();
            default : break;
        }
        throw new ChangeActionException();
    }

    /**
     * Get a position of the round tracker from the player
     * @return the given position
     * @throws ChangeActionException is called if the player decides to cahnge the action
     * @throws HaltException is called if the time is up
     */
    Coordinate getRoundTrackPosition() throws ChangeActionException, HaltException {
        int turn = -1;
        int pos = -1;
        int choice = 2;
        cliModel.showRoundTracker();
        while (choice == 2) {
            printStream.println("\n\nChoose the round index. Insert a value from 0 to 9\n");
            turn = takeInput(0, 9);
            int size = cliModel.getBoard().getRoundTracker().get(turn).size();
            printStream.println("\n\nChoose the position. Insert a value from 0 to " + (size-1)+"\n");
            printStream.print(" or [" + size + "] to change action\n");
            pos = takeInput(0, cliModel.getBoard().getRoundTracker().get(turn).size());
            if (pos == size) throw new ChangeActionException();
            else {
                printStream.print("\n\nYou selected this die: ");
                cliModel.showExtendedDice(cliModel.getBoard().getRoundTracker().get(turn).get(pos));
                printStream.println("\n\nAre you sure? \n [1] to accept  [2] to change position");
                choice = takeInput(1, 2);
            }
        }
        return new Coordinate(turn, pos);
    }

    /**
     * Get a position of the draftpool from the player
     * @return the given position
     * @throws ChangeActionException is called if the player decides to cahnge action
     * @throws HaltException is called if tipe is up
     */
    int getDraftPoolPosition() throws ChangeActionException, HaltException {
        int choice;
        int confirm;
        printStream.print("\n\nSelect the index of the die to choose. ");
        cliModel.showDraftPool();
        choice = takeInput(0, cliModel.getBoard().getDraftPool().size() - 1);
        printStream.println("\n\nYou selected this die: ");
        cliModel.showExtendedDice(cliModel.getBoard().getDraftPool().get(choice));
        printStream.println("\n\nAre you sure? \n [1] to accept  [2] to change  [3] to choose another action");
        confirm = takeInput(1, 3);
        switch(confirm) {
            case 1: return choice;
            case 2: return getDraftPoolPosition();
            default: throw new ChangeActionException();
        }
    }

    /**
     * Get a die value ferom the player (a number from 1 to 6)
     * @return the given value (1-6)
     * @throws HaltException is called if time is up
     */
    int getDieValue() throws HaltException {
        printStream.print("\n\nChoose the value of the die (value goes from 1 to 6)");
        return takeInput(1,6);
    }

    /**
     * Get from the player the decision to increment or decrement the die
     * It is used during the usage of {@link it.polimi.se2018.mvc.model.toolcards.GrozingPliers}
     * @return the decision from the player
     * @throws HaltException is called if time is up
     */
    int getIncrementOrDecrement() throws HaltException {
        printStream.println("\n\n0 to decrease, 1 to increase.");
        return takeInput(0,1);
    }

    @Override
    public void handleNetworkOutput(Message message) {
        clientConnection.sendMessage(message);
    }

    @Override
    public void handleAsyncEvent(boolean halt, String message) {
        out.println("\n"+message);
        if(halt) inputGetter.interrupt();
    }

    @Override
    public void endGame() {
        client.setGameEnded();
        stop();
    }
}
