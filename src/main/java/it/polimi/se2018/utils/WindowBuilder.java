package it.polimi.se2018.utils;


import it.polimi.se2018.model.Color;
import it.polimi.se2018.model.Square;
import it.polimi.se2018.model.Window;
import it.polimi.se2018.network.messages.Coordinate;
import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class WindowBuilder {

    private WindowBuilder() {
    }

    public static List<Pair<Window,Window>> create() {
        JSONParser parser = new JSONParser();
        List<Pair<Window,Window>> windows = new ArrayList<>();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("resources/maps.json"));
            JSONArray jsonMaps = (JSONArray) jsonObject.get("maps");
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
                windows.add(new Pair(window1, window2));
            }
            Collections.shuffle(windows);
            return windows;
        } catch (IOException | ParseException e){
            return new ArrayList<>();
        }
    }

    private static Window createMap(String title, int level, String board) {
        Square[][] squares = new Square[4][5];
        String[] tokens = board.split(",");
        int row =0;
        int col =0;
        for(String token: tokens) {
            try {
                squares[row][col]=new Square(Color.WHITE,Integer.parseInt(token),new Coordinate(row,col));
            }
            catch(NumberFormatException e) {
                squares[row][col]=new Square(Color.fromAbbreviation(token),0,new Coordinate(row,col));
            }
            if (col<4) col++;
            else if (col==4 && row<3) {
                row++;
                col=0;
            }
            else break;
        }
        return new Window(title,level,squares);
    }

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

