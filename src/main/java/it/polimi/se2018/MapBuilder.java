package it.polimi.se2018;

import it.polimi.se2018.model.Map;
import it.polimi.se2018.model.Square;
import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class MapBuilder {

    /*public static void create() {
        JSONParser parser = new JSONParser();
        ArrayList<Pair<Map,Map>> maps;
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(""));
            JSONArray jsonMaps = (JSONArray) jsonObject.get("maps");
            Iterator iterator = jsonMaps.iterator();
            while(iterator.hasNext()) {
                JSONObject jsonMapObject = (JSONObject) iterator.next();
                JSONArray jsonMapArray = (JSONArray) jsonMapObject.get("map");
                Iterator mapIterator = jsonMapArray.iterator();
                while(mapIterator.hasNext()) {
                    JSONObject singleMap = (JSONObject) mapIterator.next();
                    String title = (String) singleMap.get("title");
                    int level = (int) singleMap.get("level");
                    String board = (String) singleMap.get("board");
                    Map map = createMap(title,level,board);
                }

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Map createMap(String title, int level, String board) {
        Square[][] squares = new Square[4][5];
        String[] tokens = board.split(",");
    }*/
}

