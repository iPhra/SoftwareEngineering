package it.polimi.se2018.view.cli;

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


    Coordinate getDieInMap() {
        printStream.println("Choose the die in the window");
        return getCoordinate();
    }

    public Coordinate getCoordinate() {
        int row = -1;
        int col = -1;
        printYourWindow();
        while (row < 0 || row > 3) {
            printStream.print("Choose the row");
            row = scanner.nextInt();
        }
        while (col < 0 || col > 4) {
            printStream.print("Choose the col");
            col = scanner.nextInt();
        }
        return new Coordinate(row, col);
    }

    Coordinate getRoundTrackPosition() {
        int turn = -1;
        int pos = -1;
        printRoundTracker();
        while (turn < 1 || turn > 10) {
            printStream.print("Choose the turn.");
            turn = scanner.nextInt();
        }
        while (pos < 0 || pos > board.getRoundTracker().get(turn).size()) {
            printStream.print("Choose the position.");
            pos = scanner.nextInt();
        }
        return new Coordinate(turn, pos);
    }

    int getToolCard() {
        int choice = -1;
        while (choice < 0 || choice > toolCards.size()) {
            printToolcard();
            printStream.println("Select the toolcard");
            choice = scanner.nextInt();
        }
        return  choice;
    }

    int getValueDie() {
        int val = 0;
        while (val < 1 || val > 6) {
            printStream.print("Choose the value of the die");
            val = scanner.nextInt();
        }
        return val;
    }

    int getPositionDraftPool() {
        int choice = -1;
        printStream.print("Select the index of the die to choose.");
        while (choice < 1 || choice >= board.getDraftPool().size()) {
            printDraftPool();
            choice = scanner.nextInt();
        }
        return choice;
    }

    int getMinusPlus() {
        int choice = -1;
        while (choice < 1 || choice > 2) {
            printStream.print("Choose 1 to increase 2 to decrease");
            choice = scanner.nextInt();
        }
        return choice;
    }

    void getPlayerWindow() {
        printStream.print("Choose the player:");
        int choicePlayer = -1;
        while (choicePlayer < 0 || choicePlayer > playersName.size()) {
            for(int i = 0; i < playersName.size(); i++){
                printStream.print(i + "\b" + playersName.get(i));
            }
            choicePlayer = scanner.nextInt();
        }
        Square[][] window = board.getPlayerWindow().get(choicePlayer);
        printPlayerWindow(window);
    }

    void printDraftPool() {
        printStream.println("Dice on Draftpool are:");
        for (int i = 0; i < board.getDraftPool().size(); i++) {
            printStream.print(i + ": color: " + board.getDraftPool().get(i).getColor() + ", value: " + board.getDraftPool().get(i).getValue());
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
        Square[][] window = board.getPlayerWindow().get(yourIndex);
        for (Square[] row : window) {
            for (Square square : row) {
                printDie(square.getDie());
            }
            printStream.print("\n");
        }
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
            if (!square.getColor().equals(Color.WHITE)) {
                switch (Color.fromColor(square.getColor())) {
                    case 1:
                        toPrint = "nb\b";
                        break;
                    case 2:
                        toPrint = "nr\b";
                        break;
                    case 3:
                        toPrint = "ng\b";
                        break;
                    case 4:
                        toPrint = "ny\b";
                        break;
                    case 5:
                        toPrint = "np\b";
                        break;
                    default : break;
                }
            }
            if (square.getValue() != 0) {
                switch (getValueDie()) {
                    case 1:
                        toPrint = "n1\b";
                        break;
                    case 2:
                        toPrint = "n2\b";
                        break;
                    case 3:
                        toPrint = "n3\b";
                        break;
                    case 4:
                        toPrint = "n4\b";
                        break;
                    case 5:
                        toPrint = "n5\b";
                        break;
                    case 6:
                        toPrint = "n6\b";
                        break;
                    default : break;
                }
            }
            else {
                toPrint = "nn";
            }
        }
        printStream.print(toPrint);
    }

    private void printDie(Die die) {
        String toPrint = "";
        switch (getValueDie()) {
            case 1 : toPrint = "1"; break;
            case 2 : toPrint = "2"; break;
            case 3 : toPrint = "3"; break;
            case 4 : toPrint = "4"; break;
            case 5 : toPrint = "5"; break;
            case 6 : toPrint = "6"; break;
            default: break;
        }
        switch (Color.fromColor(die.getColor())) {
            case 1 : toPrint = toPrint + "B\b"; break;
            case 2 : toPrint = toPrint + "R\b"; break;
            case 3 : toPrint = toPrint + "G\b"; break;
            case 4 : toPrint = toPrint + "Y\b"; break;
            case 5 : toPrint = toPrint + "P\b"; break;
            default : break;
        }
        printStream.print(toPrint);
    }

    void printToolcard() {
        for (ToolCard toolcard : toolCards) {
            printStream.print(toolcard.getTitle() + "\b" + toolcard.getTitle() + "\n");
        }
    }

    void printYourFavorPoint() {
        int yourIndex = board.getPlayerID().indexOf(playerID);
        printStream.println(board.getPlayerFavorPoint().get(yourIndex));
    }

    void printPrivateObjective() {
        printStream.println(privateObjective.getTitle() + "\b" + privateObjective.getDescription());
    }

    void printPublicObjective() {
        for (PublicObjective obj : publicObjectives) {
            printStream.println(obj.getDescription());
        }
    }
}
