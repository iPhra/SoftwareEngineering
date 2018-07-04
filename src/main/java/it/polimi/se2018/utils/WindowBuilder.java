package it.polimi.se2018.utils;


import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.network.messages.Coordinate;
import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * This class extracts patterns for all Windows dynamically from a JSON file
 * Used to implement one FA (additional functionality)
 */
public class WindowBuilder {

    /**
     * This maps each colored constraint square to its image path in the folder resources
     */
    private static Map<String,String> colorPaths;

    /**
     * This maps each valued constraint square to its image path in the folder resources
     */
    private static Map<Integer,String> valuePaths;

    /**
     * This maps each level of difficulty of a Window to its image path in the folder resources
     */
    private static Map<Integer,String> levelPaths;

    private WindowBuilder() {
    }

    /**
     * Initializes all three Maps with the right image paths
     */
    private static void generatePaths() {
        colorPaths = new HashMap<>();
        valuePaths = new HashMap<>();
        levelPaths = new HashMap<>();
        for(int i=1; i<=6; i++) {
            valuePaths.put(i,"/constraints/value/"+i+".png");
        }
        for(String abbreviation: Color.getAllAbbreviations()) {
            colorPaths.put(abbreviation,"/constraints/color/"+abbreviation+".png");
        }
        for(int i=3; i<=6; i++) {
            levelPaths.put(i,"/windows_levels/level_"+i+".png");
        }
    }

    public static Map<String, String> getColorPaths() {
        generatePaths();
        return colorPaths;
    }

    public static Map<Integer, String> getValuePaths() {
        generatePaths();
        return valuePaths;
    }

    public static Map<Integer, String> getLevelPaths() {
        generatePaths();
        return levelPaths;
    }

    /**
     * Used by extractWindows(), parses the JSON and builds a list of pair of Windows
     * @return a shuffled list of all pairs of windows in the file
     */
    @SuppressWarnings("WhileLoopReplaceableByForEach")
    public static List<Pair<Window,Window>> create() {
        generatePaths();
        JSONParser parser = new JSONParser();
        List<Pair<Window,Window>> windows = new ArrayList<>();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream("windows.json")));
            JSONArray jsonMaps = (JSONArray) jsonObject.get("windows");
            Iterator iterator = jsonMaps.iterator();
            while(iterator.hasNext()) {
                JSONObject jsonMapObject = (JSONObject) iterator.next();
                JSONArray jsonMapArray = (JSONArray) jsonMapObject.get("window");

                JSONObject jsonMap1 = (JSONObject) jsonMapArray.get(0);
                JSONObject jsonMap2 = (JSONObject) jsonMapArray.get(1);

                String title1 = (String) jsonMap1.get("title");
                int level1 = (int) (long) jsonMap1.get("level");
                String board1 = (String) jsonMap1.get("board");
                String title2 = (String) jsonMap2.get("title");
                int level2 = (int) (long) jsonMap2.get("level");
                String board2 = (String) jsonMap2.get("board");

                Window window1 = createMap(title1,level1,board1);
                Window window2 = createMap(title2,level2,board2);
                windows.add(new Pair<>(window1, window2));
            }
            Collections.shuffle(windows);
            return windows;
        } catch (IOException | ParseException e){
            return new ArrayList<>();
        }
    }

    /**
     * Used by create(), creates a single Window
     * @param title is the title of the window
     * @param level is the level of the window
     * @param board is the pattern of this window
     * @return the new created Window
     */
    private static Window createMap(String title, int level, String board) {
        Square[][] squares = new Square[4][5];
        String[] tokens = board.split(",");
        int row =0;
        int col =0;
        for(String token: tokens) {
            try {
                squares[row][col]=new Square(Color.WHITE,Integer.parseInt(token),new Coordinate(row,col), valuePaths.get(Integer.parseInt(token)));
            }
            catch(NumberFormatException e) {
                squares[row][col]=new Square(Color.fromAbbreviation(token),0,new Coordinate(row,col),colorPaths.get(token));
            }
            if (col<4) col++;
            else if (col==4 && row<3) {
                row++;
                col=0;
            }
            else break;
        }
        return new Window(title,level,squares,levelPaths.get(level));
    }

    /**
     * Extracts one pair of Windows for each player in the game, used by {@link it.polimi.se2018.mvc.controller.GameManager}
     * @param players is the number of players in the game
     * @return a list of pairs
     */
    public static List<Window> extractWindows(int players) {
        List<Pair<Window, Window>> pairedWindows = WindowBuilder.create();
        List<Window> linearWindows = new ArrayList<>();
        for (int i = 0; i < 2 * players; i++) {
            linearWindows.add(pairedWindows.get(i).getKey());
            linearWindows.add(pairedWindows.get(i).getValue());
        }
        return linearWindows;
    }
}