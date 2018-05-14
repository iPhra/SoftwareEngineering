package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.ModelView;
import it.polimi.se2018.network.connections.ClientConnection;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.DraftMessage;
import it.polimi.se2018.network.messages.requests.Message;
import it.polimi.se2018.network.messages.requests.PassMessage;
import it.polimi.se2018.network.messages.requests.PlaceMessage;
import it.polimi.se2018.network.messages.responses.*;
import it.polimi.se2018.view.ClientView;

import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.*;

public class CLIClientView implements ResponseHandler, ClientView {
    private final Player player;
    private final ClientConnection clientConnection;
    private ModelView board;
    private Scanner scanner = new Scanner(System.in);
    private final OutputStream output;

    public CLIClientView(Player player, ClientConnection clientConnection, ModelView board, OutputStream output) {
        this.player = player;
        this.clientConnection = clientConnection;
        this.board = board;
        this.output = output;
    }

    @Override
    //receives input from the network, called by class clientConnection
    public void handleNetworkInput(Response response) {
        response.handle(this);
    }

    @Override
    public void handleNetworkInput(Message message) {
        //not implemented client-side
    }

    //called when i receive a TextResponse
    public void handleUserInput() {
    }

    //updates the board
    @Override
    //aggiorno il modelview e stampo a schermo la tua e il roundtracker
    public void handleResponse(ModelViewResponse modelViewResponse) {
    }

    //prints the text message
    @Override
    //eccezioni da printare
    public void handleResponse(TextResponse textResponse) {
    }

    @Override
    //chamo inizio turno
    public void handleResponse(TurnStartResponse turnStartResponse) {
    }

    @Override
    public void handleResponse(ToolCardResponse toolCardResponse) {
    }


    public void chooseAction () throws RemoteException{
        //Choose the action to do DraftDie, UseToolcard, PlaceDie, PassTurn
        int choice = -1;
        Set<Integer> choosable = new HashSet<>();
        choosable.add(4);
        if (!player.hasDraftedDie()) {
            choosable.add(1);
        }
        if (!player.hasUsedCard()) {
            choosable.add(2);
        }
        if (player.hasDieInHand()) {
            choosable.add(3);
        }
        System.out.print("It's your turn, choose your action");
        while (!choosable.contains(choice)) {
            for (int i = 1; i < 4; i++) {
                if (i == 1 && choosable.contains(i)) {
                    System.out.print("1: Draft a die");
                }
                if (i == 2 && choosable.contains(i)) {
                    System.out.print("2: Use a toolcards");
                }
                if (i == 3 && choosable.contains(i)) {
                    System.out.print("3: Place the drafted die");
                }
                System.out.print("4: Pass turn");
            }
            choice = scanner.nextInt();
        }
        if (choice == 1) {
            draftDie();
        }
        if (choice == 2) {
            useToolcard();
        }
        if (choice == 3) {
            placeDie();
        }
        if (choice == 4) {
            passTurn();
        }
    }

    public void passTurn () throws RemoteException{
        clientConnection.sendMessage(new PassMessage(player));
    }

    public void draftDie () throws RemoteException {
        System.out.print("Choose the die to draft.");
        int index = this.getPositionDraftPool();
        clientConnection.sendMessage(new DraftMessage(player, index));
    }

    public void useToolcard() throws RemoteException {
        int choice;
    }

    public void placeDie() throws RemoteException {
        System.out.print("Choose the position where put the drafted die");
        Coordinate coordinate = this.getCoordinate();
        clientConnection.sendMessage(new PlaceMessage(player, coordinate));
    }

    public Coordinate getCoordinate() {
        int row = -1, col = -1;
        //printMap(player);
        while (row < 0 || row > 3) {
            System.out.print("Choose the row");
            row = scanner.nextInt();
        }
        while (col < 0 || col > 4) {
            System.out.print("Choose the col");
            col = scanner.nextInt();
        }
        return new Coordinate(row, col);
    }

    public Coordinate getRoundTrackPosition() {
        int turn = -1, pos = -1;
        //printRoundTrack();
        while (turn < 1 || pos > 10) {
            System.out.print("Choose the turn.");
            turn = scanner.nextInt();
        }
        while (pos < 0 || pos > board.getRoundTracker()[turn].size()) {
            System.out.print("Choose the position.");
            pos = scanner.nextInt();
        }
        return new Coordinate(turn, pos);
    }

    public int getValueDie() {
        int val = 0;
        while (val < 1 || val > 6) {
            System.out.print("Choose the value of the die");
            val = scanner.nextInt();
        }
        return val;
    }

    public int getPositionDraftPool() {
        int choice = -1;
        System.out.print("Select the index of the die to choose.");
        while (choice < 1 || choice >= board.getDraftPool().size()) {
            printDraftPool();
            choice = scanner.nextInt();
        }
        return choice;
    }

    public int getMinusPlus() {
        int choice = -1;
        while (choice < 1 || choice > 2) {
            System.out.print("Choose 1 to increase 2 to decrease");
            choice = scanner.nextInt();
        }
        return choice;
    }

    public void printDraftPool() {
        System.out.print("Dice on Draftpool are:");
        for (int i = 0; i < board.getDraftPool().size(); i++) {
            System.out.print(i + ": color: " + board.getDraftPool().get(i).getColor() + ", value: " + board.getDraftPool().get(i).getValue());
        }
    }
}
