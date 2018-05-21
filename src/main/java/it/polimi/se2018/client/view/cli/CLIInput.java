package it.polimi.se2018.client.view.cli;

import it.polimi.se2018.model.*;
import it.polimi.se2018.model.objectives.privateobjectives.PrivateObjective;
import it.polimi.se2018.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.model.toolcards.ToolCard;
import it.polimi.se2018.network.messages.Coordinate;

import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.*;

public class CLIInput {
    private final int playerID;
    private List<String> playersName;
    private ModelView board;
    private Scanner scanner;
    private List<ToolCard> toolCards;
    private PrivateObjective privateObjective;
    private List<PublicObjective> publicObjectives;
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

    public Scanner getScanner() {
        return scanner;
    }

    int takeInput() {
        scanner = new Scanner(in);
        boolean iterate = true;
        int res=0;
        do {
            try {
                res = scanner.nextInt();
                iterate=false;
            } catch (InputMismatchException e) {
                printStream.println("Input is invalid");
                scanner.nextLine();
            }
        }
        while(iterate);
        return res;
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
            row = takeInput();
        }
        while (col < 0 || col > 4) {
            printStream.println("Choose the column");
            col = takeInput();
        }
        while (choice < 1 || choice > 3) {
            printStream.println("You chose the position. Press: \n [1] to accept \n [2] to change [3] to do another action");
            choice = takeInput();
            switch(choice) {
                case 1 : return new Coordinate(row,col);
                case 2 : return getCoordinate();
                case 3 : new Coordinate(-1,-1); break;
                default : break;
            }
        }
        return new Coordinate(-1, -1);
    }

    Coordinate getRoundTrackPosition() {
        int turn = -1;
        int pos = -1;
        int choice = 1;
        printRoundTracker();
        while (choice == 1) {
            while (turn < 0 || turn > 9) {
                printStream.println("Choose the turn. Insert a number from 0 to 9");
                turn = takeInput();
                while (pos < 0 || pos > board.getRoundTracker().get(turn).size() && pos != 9 && pos!=8) {
                    printStream.println("Choose the position. Insert a number from 0 to " + board.getRoundTracker().get(turn).size());
                    printStream.println("[8] to choose another turn");
                    printStream.println("[9] to change action");
                    pos = takeInput();
                }
                if (pos == 8) turn = -1;
            }
            if (pos == 9) {
                choice = 1;
                turn = -1;
            }
            else {
                printStream.print("You selected this die: ");
                printDieExtended(board.getRoundTracker().get(turn).get(pos));
                printStream.println("Are you sure? \n [1] Yes  [2]-[9] No");
                choice = takeInput();
            }
        }
        return new Coordinate(turn, pos);
    }

    int getToolCard() {
        int choice = -1;
        while (choice != 3 && (choice < 0 || choice > toolCards.size()+1 || !board.getToolCardUsability().get(choice))) {
            printToolcard();
            printStream.println("Select the tool card");
            printStream.println("[3] Choose another action");
            printStream.println("[4] Ask an information on the game");
            choice = takeInput();
            if (choice == 4) askInformation();
        }
        return  choice;
    }

    int getValueDie() {
        int val = 0;
        while (val < 1 || val > 6) {
            printStream.print("Choose the value of the die (value goes from 1 to 6)");
            val = takeInput();
        }
        return val;
    }

    int getDraftPoolPosition() {
        int choice = -1;
        int confirm = -1;
        printStream.print("Select the index of the die to choose.");
        while (choice < 0 || choice >= board.getDraftPool().size()) {
            printDraftPool();
            choice = takeInput();
        }
        while (confirm < 0 || confirm > 3) {
            printStream.println("You selected this die: ");
            printDieExtended(board.getDraftPool().get(choice));
            printStream.println("Are you sure? \n [1] to accept  \n [2] to change \n [3] to choose another action");
            confirm = takeInput();
        }
        switch(confirm) {
            case 1: return choice;
            case 2: return getDraftPoolPosition();
            default: return -1;
        }
    }

    int getMinusPlus() {
        int choice = -1;
        while (choice < 0 || choice > 1) {
            printStream.println("0 to decrease, 1 to increase.");
            choice = takeInput();
        }
        return choice == 0 ? -1:1;
    }

    private void getPlayerWindow() {
        printStream.print("Choose the player:");
        int choicePlayer = -1;
        while (choicePlayer < 0 || choicePlayer > playersName.size() - 1) {
            printStream.println("size is" + playersName.size());
            for(int i = 0; i < playersName.size(); i++){
                printStream.println(i + " " + playersName.get(i));
            }
            choicePlayer = takeInput();
        }
        printPlayerWindow(board.getPlayerWindow().get(choicePlayer));
    }

    void printDraftPool() {
        printStream.println("Dice on Draft Pool are:");
        for (int i = 0; i < board.getDraftPool().size(); i++) {
            printStream.print("[" + i + "] ");
            printDieExtended(board.getDraftPool().get(i));
        }
    }

    void printRoundTracker() {
        printStream.println("Dice on Round Tracker are:");
        for (int i = 0; i < board.getRoundTracker().size() - 1; i++) {
            for (int j = 0; j < board.getRoundTracker().get(i).size() - 1; j++) {
                printDie(board.getRoundTracker().get(i).get(j));
            }
            printStream.print("\n");
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

    public void printDieExtended(Die die) { printStream.println("Color: " + die.getColor().toString().toLowerCase() + " Value: " + die.getValue());}

    private void printDie(Die die) {
        printStream.print(die.getValue() + die.getColor().getAbbreviation() + " ");
    }

    void printToolcard() {
        printStream.println(board.getToolCardUsability().size());
        for (int i = 0; i < toolCards.size(); i++) {
            ToolCard toolCard = toolCards.get(i);
            printStream.print(i + ": " + toolCard.getTitle());
            if (!board.getToolCardUsability().get(i)) printStream.print("     (You can't use this Tool Card now!)");
            printStream.println(" ");
            printStream.println(toolCard.getDescription() + "\n");
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
        print("Choose which information you would like to know: ");
        while (choice < 1 || choice > 9 || (choice == 9 && !board.hasDieInHand())) {
            printStream.println("[1] Print your Window");
            printStream.println("[2] Print the Window of a player");
            printStream.println("[3] Print the Draft Pool");
            printStream.println("[4] Print the Round Tracker");
            printStream.println("[5] Print all Tool Cards");
            printStream.println("[6] Print all Public Objectives");
            printStream.println("[7] Print your Private Objective");
            printStream.println("[8] Print your available favor points");
            if (board.hasDieInHand()) printStream.println("[9] Print the die in your hand");
            choice = takeInput();
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
            case 9: printDieExtended(board.getDieInHand());
                break;
            default: break;
        }
    }
}
