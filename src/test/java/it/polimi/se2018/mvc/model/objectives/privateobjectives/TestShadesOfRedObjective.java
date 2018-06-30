package it.polimi.se2018.mvc.model.objectives.privateobjectives;

import it.polimi.se2018.MapDatabase;
import it.polimi.se2018.mvc.model.*;
import it.polimi.se2018.network.messages.Coordinate;
import it.polimi.se2018.utils.WindowBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestShadesOfRedObjective {
    private Square[][] matrix;
    private MapDatabase mapDatabase;
    private ShadesOfRedObjective shadesOfRedObjective;
    private Player player;

    @Before
    public void init(){
        mapDatabase = new MapDatabase();
        mapDatabase.standardWhiteMatrix();
        matrix = mapDatabase.getMatrix();
        shadesOfRedObjective =ShadesOfRedObjective.instance("imagePath");
        shadesOfRedObjective =ShadesOfRedObjective.instance("imagePath");
        Window window = new Window("BasicMap",0,matrix,WindowBuilder.getLevelPaths().get(0));
        player = new Player("name",1, window, shadesOfRedObjective);
    }

    @Test
    public void testEvalPoints(){
        assertEquals(0, shadesOfRedObjective.evalPoints(player));
        mapDatabase.sixSameColoredDice(Color.RED);
        mapDatabase.addDie(new Die(2,Color.BLUE),new Coordinate(2,3));
        matrix = mapDatabase.getMatrix();
        Window window = new Window("sixSameColoredDiceMap",0,matrix, WindowBuilder.getLevelPaths().get(0));
        player = new Player("name",1, window, shadesOfRedObjective);
        assertEquals(21, shadesOfRedObjective.evalPoints(player));
    }
}
