package it.polimi.se2018.client.view.cli;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.objectives.privateobjectives.PrivateObjective;
import it.polimi.se2018.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.model.toolcards.ToolCard;
import it.polimi.se2018.network.messages.Coordinate;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.*;

public class CLIInput {
    private final int playerID;
    private List<String> playersName;
    private ModelView board;
    private final Scanner scanner;
    private List<ToolCard> toolCards;
    private PrivateObjective privateObjective;
    private List<PublicObjective> publicObjectives;
    private Window window;
    private PrintStream printStream;

    CLIInput(int playerID) {
        this.playerID = playerID;
        scanner = new Scanner(in);
        printStream = new PrintStream(out);
    }

    public List<String> getPlayersName() {
        return playersName;
    }

    List<ToolCard> getToolCards() {
        return toolCards;
    }

    public PrivateObjective getPrivateObjective() {
        return privateObjective;
    }

    public List<PublicObjective> getPublicObjectives() {
        return publicObjectives;
    }

    public void setPlayersName(List<String> playersName) {
        this.playersName = playersName;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public void setToolCards(List<ToolCard> toolCards) {
        this.toolCards = toolCards;
    }

    public void setPrivateObjective(PrivateObjective privateObjective) {
        this.privateObjective = privateObjective;
    }

    public void setPublicObjectives(List<PublicObjective> publicObjectives) {
        this.publicObjectives = publicObjectives;
    }

    void print(String string) {
        printStream.println(string);
    }

    public void setBoard(ModelView board) {
        this.board = board;
    }

    public ModelView getBoard() {
        return board;
    }

    Coordinate getDieInMap() {
        printStream.println("Choose the die in the window");
        return getCoordinate();
    }

    public Coordinate getCoordinate() {
        int row = -1;
        int col = -1;
        int choice = -1;
        printYourWindow();
        while (row < 0 || row > 3) {
            printStream.println("Choose the row");
            row = scanner.nextInt();
        }
        while (col < 0 || col > 4) {
            printStream.println("Choose the col");
            col = scanner.nextInt();
        }
        while (choice < 1 || choice > 3) {
            printStream.println("You choose the position. Press: \n [1] to accept \n [2] to change [3] to do another action");
            choice = scanner.nextInt();
            if (choice == 1) return new Coordinate(row, col);
            else if (choice == 2) return getCoordinate();
            else if (choice == 3) new Coordinate(-1, -1);
        }
        return new Coordinate(-1, -1);
    }

    Coordinate getRoundTrackPosition() {
        int turn = -1;
        int pos = -1;
        int choice = 1;
        printRoundTracker();
        while (choice != 1) {
            while (turn < 0 || turn > 9) {
                printStream.println("Choose the turn. Insert a number from 0 to 9");
                turn = scanner.nextInt();
                while (pos < 0 || pos > board.getRoundTracker().get(turn).size() && pos != 9 && pos!=8) {
                    printStream.println("Choose the position. Insert a number from 0 to " + board.getRoundTracker().get(turn).size());
                    printStream.println("[8] to choose another turn");
                    printStream.println("[9] to don't choose a die and change action");
                    pos = scanner.nextInt();
                }
                if (pos == 8) turn = -1;
            }
            if (pos == 9) {
                choice = 1;
                turn = -1;
            }
            else {
                printStream.print("You choose this die ");
                printDieExtended(board.getRoundTracker().get(turn).get(pos));
                printStream.println("Are you sure? \n [1] Yes  [2]-[9] No");
                choice = scanner.nextInt();
            }
        }
        return new Coordinate(turn, pos);
    }

    int getToolCard() {
        int choice = -1;
        while (choice < 0 || choice > toolCards.size()+1) {
            choice = -1;
            printToolcard();
            printStream.println("Select the toolcard");
            printStream.println("[3] Choose another action");
            printStream.println("[4] Ask information of the game");
            choice = scanner.nextInt();
            if (choice == 4) askInformation();
        }
        return  choice;
    }

    int getValueDie() {
        int val = 0;
        while (val < 1 || val > 6) {
            printStream.print("Choose the value of the die (value of die go from 1 to 6)");
            val = scanner.nextInt();
        }
        return val;
    }

    int getPositionDraftPool() {
        int choice = -1;
        int confirm = -1;
        printStream.print("Select the index of the die to choose.");
        while (choice < 0 || choice >= board.getDraftPool().size()) {
            printDraftPool();
            choice = scanner.nextInt();
        }
        while (confirm < 0 || confirm > 3) {
            printStream.println("You choose this die to draft:");
            printDieExtended(board.getDraftPool().get(choice));
            printStream.println("Are you sure? \n [1] to accept  \n [2] to change \n [3] to choose another action");
            confirm = scanner.nextInt();
        }
        if (confirm == 1) return choice;
        else if (confirm == 2) return getPositionDraftPool();
        else return -1;
    }

    int getMinusPlus() {
        int choice = -1;
        while (choice < 1 || choice > 2) {
            printStream.print("Choose 1 to increase 2 to decrease.");
            choice = scanner.nextInt();
        }
        return choice;
    }

    void getPlayerWindow() {
        printStream.print("Choose the player:");
        int choicePlayer = -1;
        while (choicePlayer < 0 || choicePlayer > playersName.size() - 1) {
            printStream.println("size is" + playersName.size());
            for(int i = 0; i < playersName.size(); i++){
                printStream.println(i + " " + playersName.get(i));
            }
            choicePlayer = scanner.nextInt();
        }
        printPlayerWindow(board.getPlayerWindow().get(choicePlayer));
    }

    void printDraftPool() {
        printStream.println("Dice on Draftpool are:");
        for (int i = 0; i < board.getDraftPool().size(); i++) {
            printStream.print("[" + i + "] ");
            printDieExtended(board.getDraftPool().get(i));
        }
    }

    void printRoundTracker() {
        printStream.println("Dice on Round Tracker are:");
        for (int i = 0; i < board.getRoundTracker().size(); i++) {
            for (int j = 0; i < board.getRoundTracker().get(i).size(); j++) {
                printDie(board.getRoundTracker().get(i).get(j));
            }
        }
    }

    void printYourWindow() {
        int yourIndex = board.getPlayerID().indexOf(playerID);
        printPlayerWindow(board.getPlayerWindow().get(yourIndex));
    }

    void printPlayerWindow(Square[][] window) {
        for (Square[] row : window) {
            for (Square square : row) {
                printSquare(square);
            }
            printStream.print("\n");
        }
    }

    private void printSquare(Square square) {
        String toPrint = "";
        if (!square.isEmpty()) {
            printDie(square.getDie());
        } else {
            if (!square.getColor().equals(Color.WHITE))
                toPrint = "n" + square.getColor().getAbbreviation().toLowerCase() + " ";
            else if (square.getValue() != 0)
                toPrint = "n" + square.getValue() + " ";
            else
                toPrint = "nn ";
        }
        printStream.print(toPrint);
    }

    private void printDieExtended(Die die) { printStream.println("Color: " + die.getColor().toString().toLowerCase() + " Value: " + die.getValue());}

    private void printDie(Die die) {
        printStream.print(die.getValue() + die.getColor().getAbbreviation() + " ");
    }

    void printToolcard() {
        int i = 0;
        for (ToolCard toolcard : toolCards) {
            printStream.print(i + ": " + toolcard.getTitle() + " " + toolcard.getDescription() + "\n");
            i++;
        }
    }

    void printYourFavorPoint() {
        int yourIndex = board.getPlayerID().indexOf(playerID);
        printStream.println(board.getPlayerFavorPoint().get(yourIndex));
    }

    void printPrivateObjective() {
        printStream.println("Private Objective:");
        printStream.println(privateObjective.getTitle() + "\n" + privateObjective.getDescription() + "\n");
    }

    void printPublicObjective() {
        printStream.println("Public Objectives:");
        for (PublicObjective obj : publicObjectives) {
            printStream.println(obj.getTitle() + "\n" + obj.getDescription() + "\n");
        }
    }

    public void askInformation() {
        int choice = -1;
        print("Choose the information you need.");
        while (choice < 1 || choice > 9) {
            print("[1] Print your window");
            print("[2] Print window of a player");
            print("[3] Print draft pool");
            print("[4] Print round tracker");
            print("[5] Print toolcard");
            print("[6] Print public objective");
            print("[7] Print your private objective");
            print("[8] Print your favor points");
            choice = scanner.nextInt();
        }
        switch (choice) {
            case 1: printYourWindow();
                break;
            case 2: getPlayerWindow();
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
}
