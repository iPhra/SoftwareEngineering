package it.polimi.se2018.client.view.cli;

import it.polimi.se2018.client.network.ClientConnection;
import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.sync.SyncResponse;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.exceptions.HaltException;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.*;

public class CLIView extends Observable<SyncResponse> implements ClientView {
    private final CLIController cliController;
    private final CLIModel cliModel;
    private final PrintStream printStream;
    private final Scanner scanner;
    private boolean stopAction;
    private ClientConnection clientConnection;
    private final List<SyncResponse> events;
    private boolean isOpen;

    public CLIView(int playerID) {
        cliModel = new CLIModel(this,playerID);
        cliController = new CLIController(this, cliModel, playerID);
        register(cliController);
        scanner = new Scanner(System.in);
        printStream = new PrintStream(out);
        stopAction = false;
        events = new ArrayList<>();
        isOpen = true;
    }

    private void wakeUp() {
        synchronized (events) {
            while (events.isEmpty()) {
                try {
                    events.wait();
                } catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            }
            SyncResponse syncResponse = events.remove(0);
            notify(syncResponse);
        }
    }

    void handleNetworkOutput(Message message) {
        clientConnection.sendMessage(message);
        wakeUp();
    }

    void stopConnection() {
        clientConnection.stop();
    }

    void print(String string) { printStream.print(string); }

    int takeInput(int bottom, int top) throws HaltException {
        boolean iterate = true;
        int res=0;
        try {
            int junk = System.in.read(new byte[System.in.available()]);
        }
        catch(IOException e) {
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.ALL,e.getMessage());
        }
        do {
            try {
                res = scanner.nextInt();
                if (res <= top && res >= bottom) iterate = false;
            } catch (InputMismatchException e) {
                printStream.println("Input is invalid");
                scanner.nextLine();
            }
        }
        while(iterate);
        if (stopAction) throw new HaltException();
        return res;
    }

    Coordinate getCoordinate() throws HaltException {
        int row;
        int col;
        int choice;
        cliModel.showYourWindow();
        printStream.println("Choose the row");
        row = takeInput(0, 3);
        printStream.println("Choose the column");
        col = takeInput(0, 4);
        printStream.println("You chose the position. Press: \n [1] to accept \n [2] to change [3] to do another action");
        choice = takeInput(1, 3);
        switch(choice) {
            case 1 : return new Coordinate(row,col);
            case 2 : return getCoordinate();
            case 3 : new Coordinate(-1,-1); break;
            default : break;
        }
        return new Coordinate(-1, -1);
    }

    Coordinate getRoundTrackPosition() throws HaltException {
        int turn = -1;
        int pos = -1;
        int choice = 2;
        cliModel.showRoundTracker();
        while (choice == 2) {
            printStream.println("Choose the turn. Insert a number from 0 to 9");
            turn = takeInput(0, 9);
            int size = cliModel.getBoard().getRoundTracker().get(turn).size();
            printStream.println("Choose the position. Insert a number from 0 to " + size);
            printStream.println("[" + size + "] to choose another turn");
            printStream.println("[" + (size + 1) + "] to change action");
            pos = takeInput(0, cliModel.getBoard().getRoundTracker().get(turn).size() + 1);
            if (pos == size) turn = -1;
            else if (pos == (size + 1)) {
                choice = 1;
                turn = -1;
            }
            else {
                printStream.print("You selected this die: ");
                cliModel.showExtendedDice(cliModel.getBoard().getRoundTracker().get(turn).get(pos));
                printStream.println("Are you sure? \n [1] Yes  [2] No");
                choice = takeInput(1, 2);
            }
        }
        return new Coordinate(turn, pos);
    }

    int getDieValue() throws HaltException {
        int val;
        printStream.print("Choose the value of the die (value goes from 1 to 6)");
        val = takeInput(1, 6);
        return val;
    }

    int getDraftPoolPosition() throws HaltException {
        int choice;
        int confirm;
        printStream.print("Select the index of the die to choose. ");
        cliModel.showDraftPool();
        choice = takeInput(0, cliModel.getBoard().getRoundTracker().size() - 1);
        printStream.println("You selected this die: ");
        cliModel.showExtendedDice(cliModel.getBoard().getDraftPool().get(choice));
        printStream.println("Are you sure? \n [1] to accept  [2] to change \n [3] to choose another action");
        confirm = takeInput(0, 3);
        switch(confirm) {
            case 1: return choice;
            case 2: return getDraftPoolPosition();
            default: return -1;
        }
    }

    int getIncrementOrDecrement() throws HaltException {
        int choice;
        printStream.println("0 to decrease, 1 to increase.");
        choice = takeInput(0, 1);
        return choice == 0 ? -1:1;
    }

    public void setStopAction(boolean stopAction) {
        this.stopAction = stopAction;
    }

    public void setIsOpen(Boolean value) {
        isOpen = value;
    }

    public void start() {
        while(isOpen) {
            wakeUp();
        }
    }

    @Override
    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
        clientConnection.setCLIController(cliController);
    }

    @Override
    //receives input from the network, called by class clientConnection
    public void handleNetworkInput(SyncResponse syncResponse) {
        synchronized (events) {
            events.add(syncResponse);
            events.notifyAll();
        }
    }
}
