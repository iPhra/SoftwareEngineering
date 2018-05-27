package it.polimi.se2018.client.view.cli;

import it.polimi.se2018.mvc.controller.ModelView;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.objectives.privateobjectives.PrivateObjective;
import it.polimi.se2018.mvc.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.utils.exceptions.TimeoutException;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.*;

public class CLIInput {
    private final int playerID;
    private List<String> playerNames;
    private ModelView board;
    private List<ToolCard> toolCards;
    private PrivateObjective privateObjective;
    private List<PublicObjective> publicObjectives;
    private final PrintStream printStream;
    private final Scanner scanner;
    private boolean timeIsUp;

    CLIInput(int playerID) {
        scanner = new Scanner(System.in);
        this.playerID = playerID;
        printStream = new PrintStream(out);
        timeIsUp = false;
    }

    List<ToolCard> getToolCards() {
        return toolCards;
    }

    public List<PublicObjective> getPublicObjectives() {
        return publicObjectives;
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

    private String generateSpaces(int number) {
        StringBuilder res = new StringBuilder();
        for(int i=0; i<number; i++) {
            res.append(" ");
        }
        return res.toString();
    }

    public void setTimeIsUp(boolean timeIsUp) { this.timeIsUp = timeIsUp; }

    void print(String string) { printStream.println(string); }

    public void setBoard(ModelView board) {
        if(playerNames==null) playerNames = board.getPlayerNames();
        this.board = board;
    }

    public ModelView getBoard() {
        return board;
    }

    int takeInput() throws TimeoutException {
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
                iterate = false;
            } catch (InputMismatchException e) {
                printStream.println("Input is invalid");
                scanner.nextLine();
            }
        }
        while(iterate);
        if (timeIsUp) throw new TimeoutException();
        return res;
    }

    public Coordinate getCoordinate() throws TimeoutException {
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

    Coordinate getRoundTrackPosition() throws TimeoutException {
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
                printDraftPoolDice(board.getRoundTracker().get(turn).get(pos));
                printStream.println("Are you sure? \n [1] Yes  [2]-[9] No");
                choice = takeInput();
            }
        }
        return new Coordinate(turn, pos);
    }

    int getToolCard() throws TimeoutException {
        int choice = -1;
        printToolCards();
        printStream.println("Select the tool card");
        printStream.println("[3] Choose another action");
        printStream.println("[4] Print the state of the game");
        while (choice != 3 && (choice < 0 || choice > toolCards.size()+1 || !board.getToolCardUsability().get(choice))) {
            choice = takeInput();
            if (choice == 4) printModel();
            if (choice >= 0 && choice < 3 && !board.getToolCardUsability().get(choice)) {
                printStream.println("You can't use the chosen tool card. Please choose another one.");
            }
        }
        return  choice;
    }

    int getDieValue() throws TimeoutException {
        int val = 0;
        while (val < 1 || val > 6) {
            printStream.print("Choose the value of the die (value goes from 1 to 6)");
            val = takeInput();
        }
        return val;
    }

    int getDraftPoolPosition() throws TimeoutException {
        int choice = -1;
        int confirm = -1;
        printStream.print("Select the index of the die to choose. ");
        while (choice < 0 || choice >= board.getDraftPool().size()) {
            printDraftPool();
            choice = takeInput();
        }
        while (confirm < 0 || confirm > 3) {
            printStream.println("You selected this die: ");
            printDraftPoolDice(board.getDraftPool().get(choice));
            printStream.println("Are you sure? \n [1] to accept  [2] to change \n [3] to choose another action");
            confirm = takeInput();
        }
        switch(confirm) {
            case 1: return choice;
            case 2: return getDraftPoolPosition();
            default: return -1;
        }
    }

    int getIncrementOrDecrement() throws TimeoutException {
        int choice = -1;
        while (choice < 0 || choice > 1) {
            printStream.println("0 to decrease, 1 to increase.");
            choice = takeInput();
        }
        return choice == 0 ? -1:1;
    }

    void printDraftPool() {
        printStream.println("Dice on Draft Pool are:");
        for (int i = 0; i < board.getDraftPool().size(); i++) {
            printStream.print("[" + i + "] ");
            printDraftPoolDice(board.getDraftPool().get(i));
            if ((i+1) % 3 == 0) printStream.println(" ");
        }
        printStream.println("\n");
    }

    private void printRoundTracker() {
        printStream.println("Dice on Round Tracker are:");
        for (int i = 0; i < board.getRoundTracker().size(); i++) {
            printStream.print("Round " + i + ":  ");
            for (int j = 0; j < board.getRoundTracker().get(i).size(); j++) {
                printStream.print(board.getRoundTracker().get(i).get(j));
            }
            printStream.print("\n");
        }
        printStream.println("\n");
    }

    private void printYourWindow() {
        int yourIndex = board.getPlayerID().indexOf(playerID);
        Square[][] window = board.getPlayerWindow().get(yourIndex);
        StringBuilder builder;
        for (Square[] row : window) {
            builder = new StringBuilder();
            for (Square square : row) {
                builder.append(square);
            }
            printStream.print(builder);
            printStream.print("\n");
        }
    }

    private void printWindows(List<Square[][]> windows) {
        StringBuilder builder;
        for(int i=0; i<4; i++) {
            builder = new StringBuilder();
            for(Square[][] window : windows) {
                for(Square square : window[i]) {
                    builder.append(square.toString());
                    builder.append(" ");
                }
                builder.append(generateSpaces(20));
            }
            printStream.println(builder);
        }
    }

    void printSetupWindows(List<Window> windows) {
        printStream.println("Windows:");
        StringBuilder titles = new StringBuilder();
        String tmp;
        for(int i=0; i<windows.size(); i++) {
            tmp = "[" + (i+1) + "] " + windows.get(i).getTitle() + generateSpaces(36-windows.get(i).getTitle().length());
            titles.append(tmp);
        }
        printStream.println(titles);
        printWindows(getPatterns(windows));
        StringBuilder levels = new StringBuilder();
        for(Window window : windows) {
            tmp = "Level: "+window.getLevel() + generateSpaces(32);
            levels.append(tmp);
        }
        printStream.println(levels);
    }

    private List<Square[][]> getPatterns(List<Window> windows) {
        List<Square[][]> result = new ArrayList<>();
        for(Window window : windows) {
            result.add(window.modelViewCopy());
        }
        return result;
    }

    private void printPlayersWindows() {
        printStream.println("Windows:");
        printWindows(board.getPlayerWindow());
        StringBuilder builder = new StringBuilder();
        String tmp;
        for(String playerName: playerNames) {
            tmp = "Player "+ playerName + generateSpaces(33-playerName.length());
            builder.append(tmp);
        }
        printStream.println(builder);
    }

    public void printDraftPoolDice(Die die) {
        StringBuilder result = new StringBuilder();
        String color = die.getColor().toString();
        result.append("COLOR: ");
        result.append(color.toLowerCase());
        result.append(generateSpaces(6 - color.length()));
        result.append(generateSpaces(2));
        result.append(" VALUE: ");
        result.append(die.getValue());
        result.append(generateSpaces(5));
        printStream.print(result);
    }

    private void printToolCards() {
        printStream.println("Tool Cards:");
        StringBuilder result;
        for (int i = 0; i < toolCards.size(); i++) {
            result = new StringBuilder();
            ToolCard toolCard = toolCards.get(i);
            result.append(i);
            result.append(": ");
            result.append(toolCard);
            if (!board.getToolCardUsability().get(i)) {
                result.append("You can't use this Tool Card right now!");
                result.append("\n");
            }
            result.append("\n");
            printStream.print(result.toString());
        }
        printStream.print("\n");
    }

    private void printFavorPoints() {
        int yourIndex = board.getPlayerID().indexOf(playerID);
        printStream.println("Favor points left: " + board.getPlayerFavorPoint().get(yourIndex));
        printStream.println("\n");
    }

    void printPrivateObjective() {
        printStream.print("\n");
        printStream.println(privateObjective);
        printStream.println("\n");
    }

    void printPublicObjective() {
        printStream.println("Public Objectives: ");
        for (PublicObjective obj : publicObjectives) {
            printStream.print(obj);
        }
        printStream.println("\n");
    }

    public void printModel() {
        printPrivateObjective();
        printPublicObjective();
        printFavorPoints();
        printToolCards();
        printRoundTracker();
        printDraftPool();
        printPlayersWindows();
        printStream.print("\n");
    }
}
