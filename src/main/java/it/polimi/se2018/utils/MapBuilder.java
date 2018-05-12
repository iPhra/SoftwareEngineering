package it.polimi.se2018.utils;


import it.polimi.se2018.model.Color;
import it.polimi.se2018.model.Map;
import it.polimi.se2018.model.Square;
import it.polimi.se2018.network.messages.Coordinate;
import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MapBuilder {

    public static List<Pair<Map,Map>> create() {
        JSONParser parser = new JSONParser();
        List<Pair<Map,Map>> maps = new ArrayList<>();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("resources/maps.json"));
            JSONArray jsonMaps = (JSONArray) jsonObject.get("maps");
            Iterator iterator = jsonMaps.iterator();
            while(iterator.hasNext()) {
                JSONObject jsonMapObject = (JSONObject) iterator.next();
                JSONArray jsonMapArray = (JSONArray) jsonMapObject.get("map");

                JSONObject jsonMap1 = (JSONObject) jsonMapArray.get(0);
                JSONObject jsonMap2 = (JSONObject) jsonMapArray.get(1);

                String title1 = (String) jsonMap1.get("title");
                int level1 = (int) (long) jsonMap1.get("level");
                String board1 = (String) jsonMap1.get("board");
                String title2 = (String) jsonMap2.get("title");
                int level2 = (int) (long) jsonMap2.get("level");
                String board2 = (String) jsonMap2.get("board");

                Map map1 = createMap(title1,level1,board1);
                Map map2 = createMap(title2,level2,board2);
                maps.add(new Pair(map1,map2));
            }
            return maps;
        } catch (IOException | ParseException e){
            e.printStackTrace();
            return null;
        }
    }

    private static Map createMap(String title, int level, String board) {
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
        return new Map(title,level,squares);
    }
}

