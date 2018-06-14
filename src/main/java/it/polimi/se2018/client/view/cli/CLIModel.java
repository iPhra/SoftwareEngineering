package it.polimi.se2018.client.view.cli;

import it.polimi.se2018.client.view.ClientModel;
import it.polimi.se2018.mvc.model.Die;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.objectives.publicobjectives.PublicObjective;
import it.polimi.se2018.mvc.model.toolcards.ToolCard;

import java.util.ArrayList;
import java.util.List;

class CLIModel extends ClientModel {
    private final CLIView cliView;

    CLIModel(CLIView cliView, int playerID) {
        super(playerID);
        this.cliView = cliView;
    }

    private StringBuilder generateUpperDashes() {
        StringBuilder result = new StringBuilder();
        result.append("╔");
        for(int i=0; i<15; i++) {
            result.append("═");
        }
        result.append("╗");
        return result;
    }

    private StringBuilder generateLowerDashes() {
        StringBuilder result = new StringBuilder();
        result.append("╚");
        for(int i=0; i<15; i++) {
            result.append("═");
        }
        result.append("╝");
        return result;
    }

    private StringBuilder generateAllDashes(boolean upper, int windowsNumber) {
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<windowsNumber; i++) {
            if(upper) builder.append(generateUpperDashes());
            else builder.append(generateLowerDashes());
            builder.append(generateSpaces(23));
        }
        return builder;
    }

    private String generateSpaces(int number) {
        StringBuilder res = new StringBuilder();
        for(int i=0; i<number; i++) {
            res.append(" ");
        }
        return res.toString();
    }

    //prints all windows in the list
    private void showWindows(List<Square[][]> windows, int windowsNumber) {
        StringBuilder builder = generateAllDashes(true,windowsNumber);
        cliView.print(builder+"\n");
        for(int i=0; i<4; i++) {
            builder = new StringBuilder();
            for(Square[][] window : windows) {
                builder.append("║");
                for(Square square : window[i]) {
                    builder.append(square);
                }
                builder.append("║");
                builder.append(generateSpaces(23));
            }
            cliView.print(builder+"\n");
        }
        builder = generateAllDashes(false,windowsNumber);
        cliView.print(builder+"\n");
    }

    private List<Square[][]> getPatterns(List<Window> windows) {
        List<Square[][]> result = new ArrayList<>();
        for(Window window : windows) {
            result.add(window.modelViewCopy());
        }
        return result;
    }

    private StringBuilder createRowIndex() {
        StringBuilder result = new StringBuilder();
        result.append(generateSpaces(4));
        for(int i=0; i<5; i++) {
            result.append(i);
            result.append("  ");
        }
        result.append("\n");
        return result;
    }

    void showYourWindow() {
        int yourIndex = board.getPlayerID().indexOf(playerID);
        Square[][] window = board.getPlayerWindows().get(yourIndex);
        cliView.print("\n\nYour window is:\n\n");
        StringBuilder builder = createRowIndex();
        builder.append("  ");
        builder.append(generateUpperDashes());
        cliView.print(builder.toString()+"\n");
        int col = 0;
        for (Square[] row : window) {
            builder = new StringBuilder();
            builder.append(col);
            builder.append(" ║");
            for (Square square : row) {
                builder.append(square);
            }
            builder.append("║");
            cliView.print(builder.toString()+"\n");
            col++;
        }
        builder = new StringBuilder().append("  ");
        builder.append(generateLowerDashes());
        cliView.print(builder.toString()+"\n\n");
    }

    void showSetupWindows(List<Window> windows) {
        cliView.print("Windows:\n");
        StringBuilder titles = new StringBuilder();
        String tmp;
        for(int i=0; i<windows.size(); i++) {
            tmp = "[" + (i+1) + "] " + windows.get(i).getTitle() + generateSpaces(36-windows.get(i).getTitle().length());
            titles.append(tmp);
        }
        cliView.print(titles+"\n");
        showWindows(getPatterns(windows),4);
        StringBuilder levels = new StringBuilder();
        for(Window window : windows) {
            tmp = "Level: "+window.getLevel() + generateSpaces(32);
            levels.append(tmp);
        }
        cliView.print(levels+"\n\n");
    }

    void showPlayersWindows() {
        cliView.print("Windows:\n");
        StringBuilder builder = new StringBuilder();
        String tmp;
        for(String playerName: playerNames) {
            tmp = "Player "+ playerName + generateSpaces(33-playerName.length());
            builder.append(tmp);
        }
        cliView.print(builder.toString()+"\n");
        showWindows(board.getPlayerWindows(),playersNumber);
        cliView.print("\n");
    }

    void showExtendedDice(Die die) {
        StringBuilder result = new StringBuilder();
        String color = die.getColor().toString();
        result.append("COLOR: ");
        result.append(color.toLowerCase());
        result.append(generateSpaces(6 - color.length()));
        result.append(generateSpaces(2));
        result.append(" VALUE: ");
        result.append(die.getValue());
        result.append(generateSpaces(5));
        cliView.print(result.toString());
    }

    void showToolCards() {
        cliView.print("Tool Cards:\n");
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
            cliView.print(result.toString());
        }
        cliView.print("\n");
    }

    void showFavorPoints() {
        int yourIndex = board.getPlayerID().indexOf(playerID);
        cliView.print("Favor points left: " + board.getPlayerFavorPoint().get(yourIndex));
        cliView.print("\n\n\n");
    }

    void showPrivateObjective() {
        cliView.print("\n");
        cliView.print(privateObjective.toString());
        cliView.print("\n\n");
    }

    void showPublicObjectives() {
        cliView.print("Public Objectives:\n");
        for (PublicObjective obj : publicObjectives) {
            cliView.print(obj.toString());
        }
        cliView.print("\n\n");
    }

    void showDraftPool() {
        cliView.print("Dice on Draft Pool are:\n");
        for (int i = 0; i < board.getDraftPool().size(); i++) {
            cliView.print("[" + i + "] ");
            showExtendedDice(board.getDraftPool().get(i));
            if((i+1)%3 == 0) cliView.print("\n");
        }
        cliView.print("\n\n");
    }

    void showRoundTracker() {
        cliView.print("Dice on Round Tracker are:\n");
        for (int i = 0; i < board.getRoundTracker().size(); i++) {
            cliView.print("Round " + i + ":  ");
            for (int j = 0; j < board.getRoundTracker().get(i).size(); j++) {
                cliView.print(board.getRoundTracker().get(i).get(j).toString());
            }
            cliView.print("\n");
        }
        cliView.print("\n\n");
    }

    void showModel() {
        showPrivateObjective();
        showPublicObjectives();
        showFavorPoints();
        showToolCards();
        showRoundTracker();
        showDraftPool();
        showPlayersWindows();
        cliView.print("\n\n");
    }
}