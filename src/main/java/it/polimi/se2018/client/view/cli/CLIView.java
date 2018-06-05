package it.polimi.se2018.client.view.cli;

import it.polimi.se2018.client.Client;
import it.polimi.se2018.client.network.ClientConnection;
import it.polimi.se2018.client.view.ClientView;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.responses.sync.SyncResponse;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.exceptions.ChangeActionException;
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
    private final Client client;
    private final CLIController cliController;
    private final CLIModel cliModel;
    private final PrintStream printStream;
    private final Scanner scanner;
    private boolean stopAction;
    private ClientConnection clientConnection;
    private final List<SyncResponse> events;
    private boolean isOpen;

    public CLIView(Client client, int playerID) {
        this.client = client;
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

    void endGame() {
        stop();
        clientConnection.stop();
        client.startNewGame();
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
                else throw new InputMismatchException();
            } catch (InputMismatchException e) {
                printStream.println("Input is invalid");
                scanner.nextLine();
            }
        }
        while(iterate);
        if (stopAction) throw new HaltException();
        return res;
    }

    Coordinate getCoordinate() throws HaltException, ChangeActionException {
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

    Coordinate getRoundTrackPosition() throws HaltException, ChangeActionException {
        int turn = -1;
        int pos = -1;
        int choice = 2;
        cliModel.showRoundTracker();
        while (choice == 2) {
            printStream.println("Choose the turn. Insert a value from 0 to 9");
            turn = takeInput(0, 9);
            int size = cliModel.getBoard().getRoundTracker().get(turn).size();
            printStream.println("Choose the position. Insert a value from 0 to " + size);
            printStream.println("[" + size + "] to choose another turn");
            printStream.println("[" + (size + 1) + "] to change action");
            pos = takeInput(0, cliModel.getBoard().getRoundTracker().get(turn).size() + 1);
            if (pos == size || pos==(size+1)) throw new ChangeActionException();
            else {
                printStream.print("You selected this die: ");
                cliModel.showExtendedDice(cliModel.getBoard().getRoundTracker().get(turn).get(pos));
                printStream.println("Are you sure? \n [1] Yes  [2] No");
                choice = takeInput(1, 2);
            }
        }
        return new Coordinate(turn, pos);
    }

    int getDraftPoolPosition() throws HaltException, ChangeActionException {
        int choice;
        int confirm;
        printStream.print("Select the index of the die to choose. ");
        cliModel.showDraftPool();
        choice = takeInput(0, cliModel.getBoard().getDraftPool().size() - 1);
        printStream.println("You selected this die: ");
        cliModel.showExtendedDice(cliModel.getBoard().getDraftPool().get(choice));
        printStream.println("Are you sure? \n [1] to accept  [2] to change \n [3] to choose another action");
        confirm = takeInput(0, 3);
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

    public void setStopAction(boolean stopAction) {
        this.stopAction = stopAction;
    }

    public void start() {
        while(isOpen) {
            wakeUp();
        }
    }

    @Override
    public void stop() {
        isOpen=false;
    }

    @Override
    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    @Override
    //receives input from the network, called by class clientConnection
    public void handleNetworkInput(SyncResponse syncResponse) {
        synchronized (events) {
            events.add(syncResponse);
            events.notifyAll();
        }
    }

    @Override
    public synchronized void handleAsyncEvent(boolean halt, String message) {
        out.println("\n"+message);
        if(halt) {
            cliController.halt("");
        }
    }

}
