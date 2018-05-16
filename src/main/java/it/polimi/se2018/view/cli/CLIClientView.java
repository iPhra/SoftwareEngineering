package it.polimi.se2018.view.cli;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.ModelView;
import it.polimi.se2018.model.Square;
import it.polimi.se2018.model.objectives.privateobjectives.PrivateObjective;
import it.polimi.se2018.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.model.toolcards.ToolCard;
import it.polimi.se2018.network.connections.ClientConnection;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.network.messages.requests.*;
import it.polimi.se2018.network.messages.responses.*;
import it.polimi.se2018.view.ClientView;

import java.io.PrintWriter;
import java.io.Writer;
import java.rmi.RemoteException;
import java.util.*;

public class CLIClientView implements ResponseHandler, ClientView {
    private transient final int playerID;
    private transient List<String> playersName;
    private transient ClientConnection clientConnection;
    private transient ModelView board;
    private transient final Scanner scanner;
    private transient final Writer writer;
    private transient List<ToolCard> toolCards;
    private transient PrivateObjective privateObjective;
    private transient List<PublicObjective> publicObjectives;

    public CLIClientView(int playerID) {
        this.playerID = playerID;
        scanner = new Scanner(System.in);
        writer = new PrintWriter(System.out);
    }

    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
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
        this.board = modelViewResponse.getModelView();
        printRoundTracker();
    }

    //prints the text message
    @Override
    //eccezioni da printare
    public void handleResponse(TextResponse textResponse) {
        System.out.println(textResponse.getMessage());
    }

    @Override
    //chamo inizio turno
    public void handleResponse(TurnStartResponse turnStartResponse) {
        try {
            chooseAction();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleResponse(ToolCardResponse toolCardResponse) {
    }


    public void chooseAction() throws RemoteException{
        //Choose the action to do DraftDie, UseToolcard, PlaceDie, PassTurn
        int choice = -1;
        Set<Integer> choosable = new HashSet<>();
        choosable.add(4);
        choosable.add(5);
        if (!board.hasDraftedDie()) {
            choosable.add(1);
        }
        if (!board.hasUsedCard()) {
            choosable.add(3);
        }
        if (board.hasDieInHand()) {
            choosable.add(2);
        }
        System.out.print("It's your turn, choose your action");
        while (!choosable.contains(choice)) {
            for (int i = 1; i < 6; i++) {
                if (i == 1 && choosable.contains(i)) {
                    System.out.print("1: Draft a die");
                }
                if (i == 2 && choosable.contains(i)) {
                    System.out.print("2: Place the drafted die");
                }
                if (i == 3 && choosable.contains(i)) {
                    System.out.print("3: Select a toolcard");
                }
                System.out.print("4: Pass turn");
                System.out.println("5: Ask for information of the game");
            }
            choice = scanner.nextInt();
        }
        if (choice == 1) {
            draftDie();
        }
        if (choice == 2) {
            placeDie();
        }
        if (choice == 3) {
            selectToolcard();
        }
        if (choice == 4) {
            passTurn();
        }
        if (choice == 5) {
            askInformation();
        }
    }

    public void passTurn() throws RemoteException{
        clientConnection.sendMessage(new PassMessage(playerID));
    }

    public void draftDie() throws RemoteException {
        System.out.print("Choose the die to draft.");
        int index = this.getPositionDraftPool();
        clientConnection.sendMessage(new DraftMessage(playerID, index));
    }

    public void selectToolcard() throws RemoteException {
        int toolCard = getToolCard();
        clientConnection.sendMessage(new ToolCardRequestMessage(playerID, toolCard));
    }

    public void useToolcard(int indexOfToolCard) throws RemoteException {
        ToolCardMessage toolCardMessage = toolCards.get(indexOfToolCard).getPlayerRequests();
        clientConnection.sendMessage(toolCardMessage);
    }

    public void placeDie() throws RemoteException {
        System.out.print("Choose the position where put the drafted die");
        Coordinate coordinate = this.getCoordinate();
        clientConnection.sendMessage(new PlaceMessage(playerID, coordinate));
    }

    public void askInformation() {
        int choice = -1;
        System.out.println("Choose the information you need.");
        while (choice < 1 || choice > 9) {
            System.out.println("1: Print your map");
            System.out.println("2: Print map of a player");
            System.out.println("3: Print draft pool");
            System.out.println("4: Print Round Tracker");
            System.out.println("5: Print toolcard");
            System.out.println("6: Print public objective");
            System.out.println("7: Print your private objecgive");
            System.out.println("8: Print your favor points");
            choice = scanner.nextInt();
        }
        switch (choice) {
            case 1: printYourMap();
                    break;
            case 2: printPlayerMap();
                    break;
            case 3: printDraftPool();
                    break;
            case 4: printRoundTracker();
                    break;
            case 5: printToolcard();
                    break;
            case 6: printPublicObjective();
                    break;
            case 7: printPrivateObjective();
                    break;
            case 8: printYourFavorPoint();
                    break;
            default: break;
        }
    }

    public Coordinate getDieInMap() {
        System.out.println("Choose the die in the map");
        return getCoordinate();
    }

    public Coordinate getCoordinate() {
        int row = -1;
        int col = -1;
        printYourMap();
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
        int turn = -1;
        int pos = -1;
        printRoundTracker();
        while (turn < 1 || pos > 10) {
            System.out.print("Choose the turn.");
            turn = scanner.nextInt();
        }
        while (pos < 0 || pos > board.getRoundTracker().get(turn).size()) {
            System.out.print("Choose the position.");
            pos = scanner.nextInt();
        }
        return new Coordinate(turn, pos);
    }

    public int getToolCard() {
        int choice = -1;
        while (choice < 0 || choice > toolCards.size()) {
            printToolcard();
            System.out.println("Select the toolcard");
            choice = scanner.nextInt();
        }
        return  choice;
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
        System.out.println("Dice on Draftpool are:");
        for (int i = 0; i < board.getDraftPool().size(); i++) {
            System.out.print(i + ": color: " + board.getDraftPool().get(i).getColor() + ", value: " + board.getDraftPool().get(i).getValue());
        }
    }

    public void printRoundTracker() {
        System.out.println("Dice on Round Tracker are:");
        for (int i = 0; i < board.getRoundTracker().size(); i++) {
            for (int j = 0; i < board.getRoundTracker().get(i).size(); j++) {
                printDie(board.getRoundTracker().get(i).get(j));
            }
        }
    }

    public void printYourMap() {
        int yourIndex = board.getPlayerID().indexOf(playerID);
        Square[][] map = board.getPlayerMap().get(yourIndex);
        for (Square[] row : map) {
            for (Square square : row) {
                printDie(square.getDie());
            }
            System.out.print("\n");
        }
    }

    public void printPlayerMap() {
        System.out.print("Choose the player:");
        int choicePlayer = -1;
        while (choicePlayer < 0 || choicePlayer > playersName.size()) {
            System.out.print(choicePlayer +  "\b" + playersName.get(choicePlayer));
            choicePlayer = scanner.nextInt();
        }
        Square[][] map = board.getPlayerMap().get(choicePlayer);
        for (Square[] row : map) {
            for (Square square : row) {
                printDie(square.getDie());
            }
            System.out.print("\n");
        }
    }

    public void printDie(Die die) {
    }

    public void printToolcard() {
        for (ToolCard toolcard : toolCards) {
            System.out.print(toolcard.getTitle() + "\b" + toolcard.getTitle() + "\n");
        }
    }

    public void printYourFavorPoint() {
        int yourIndex = board.getPlayerID().indexOf(playerID);
        System.out.println(board.getPlayerFavorPoint().get(yourIndex));
    }

    public void printPrivateObjective() {

        System.out.println(privateObjective.getTitle() + "\b" + privateObjective.getDescription());
    }

    public void printPublicObjective() {}

}
