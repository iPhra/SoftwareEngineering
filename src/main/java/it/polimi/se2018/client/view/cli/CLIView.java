package it.polimi.se2018.client.view.cli;

import it.polimi.se2018.client.Client;
import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.utils.exceptions.ChangeActionException;
import it.polimi.se2018.utils.exceptions.HaltException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.*;

public class CLIView extends ClientView {
    private final CLIModel cliModel;
    private final PrintStream printStream;
    private final BufferedReader bufferedReader;
    private Thread currentThread;

    public CLIView(Client client, int playerID) {
        super(client);
        cliModel = new CLIModel(this,playerID);
        CLIController cliController = new CLIController(this, cliModel, playerID);
        register(cliController);
        printStream = new PrintStream(out);
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    void print(String string) { printStream.print(string); }

    synchronized int takeInput(int bottom, int top) throws HaltException {
        isIterating = true;
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
                break;
            }
            catch(IOException | NumberFormatException e) {
                printStream.println("Input is invalid");
            }
            catch(InterruptedException e) {
                throw new HaltException();
            }
        }
        while(isIterating);
        return res;
    }

    Coordinate getCoordinate() throws ChangeActionException, HaltException {
        cliModel.showYourWindow();
        printStream.println("Choose the row");
        int row = takeInput(0, 3);
        printStream.println("Choose the column");
        int col = takeInput(0, 4);
        printStream.println("You chose the position. Press: \n [1] to accept \n [2] to change [3] to do another action");
        int choice = takeInput(1, 3);
        switch(choice) {
            case 1 : return new Coordinate(row,col);
            case 2 : return getCoordinate();
            default : break;
        }
        throw new ChangeActionException();
    }

    Coordinate getRoundTrackPosition() throws ChangeActionException, HaltException {
        int turn = -1;
        int pos = -1;
        int choice = 2;
        cliModel.showRoundTracker();
        while (choice == 2) {
            printStream.println("Choose the round index. Insert a value from 0 to 9");
            turn = takeInput(0, 9);
            int size = cliModel.getBoard().getRoundTracker().get(turn).size();
            printStream.println("Choose the position. Insert a value from 0 to " + (size-1));
            printStream.println("[" + size + "] to change action");
            pos = takeInput(0, cliModel.getBoard().getRoundTracker().get(turn).size());
            if (pos == size) throw new ChangeActionException();
            else {
                printStream.print("You selected this die: ");
                cliModel.showExtendedDice(cliModel.getBoard().getRoundTracker().get(turn).get(pos));
                printStream.println("Are you sure? \n [1] to accept  [2] to change position");
                choice = takeInput(1, 2);
            }
        }
        return new Coordinate(turn, pos);
    }

    int getDraftPoolPosition() throws ChangeActionException, HaltException {
        int choice;
        int confirm;
        printStream.print("Select the index of the die to choose. ");
        cliModel.showDraftPool();
        choice = takeInput(0, cliModel.getBoard().getDraftPool().size() - 1);
        printStream.println("You selected this die: ");
        cliModel.showExtendedDice(cliModel.getBoard().getDraftPool().get(choice));
        printStream.println("Are you sure? \n [1] to accept  [2] to change \n [3] to choose another action");
        confirm = takeInput(1, 3);
        switch(confirm) {
            case 1: return choice;
            case 2: return getDraftPoolPosition();
            default: throw new ChangeActionException();
        }
    }

    int getDieValue() throws HaltException {
        printStream.print("Choose the value of the die (value goes from 1 to 6)");
        return takeInput(1,6);
    }

    int getIncrementOrDecrement() throws HaltException {
        printStream.println("0 to decrease, 1 to increase.");
        return takeInput(0,1);
    }

    public void setCurrentThread(Thread currentThread) {
        this.currentThread = currentThread;
    }

    @Override
    public void handleNetworkOutput(Message message) {
        clientConnection.sendMessage(message);
        wakeUp();
    }

    @Override
    public void handleAsyncEvent(boolean halt, String message) {
        out.println("\n"+message);
        if(halt)currentThread.interrupt();
    }
}
