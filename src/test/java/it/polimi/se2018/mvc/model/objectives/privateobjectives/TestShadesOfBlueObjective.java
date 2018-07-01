package it.polimi.se2018.mvc.model.objectives.privateobjectives;

import it.polimi.se2018.MapDatabase;
import it.polimi.se2018.mvc.model.Color;
import it.polimi.se2018.mvc.model.Window;
import it.polimi.se2018.mvc.model.Player;
import it.polimi.se2018.mvc.model.Square;
import it.polimi.se2018.utils.WindowBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestShadesOfBlueObjective {
    private Square[][] matrix;
    private MapDatabase mapDatabase;
    private ShadesOfBlueObjective shadesOfBlueObjective;
    private Player player;

    @Before
    public void init(){
        mapDatabase = new MapDatabase();
        mapDatabase.standardWhiteMatrix();
        matrix = mapDatabase.getMatrix();
        shadesOfBlueObjective=ShadesOfBlueObjective.instance();
        shadesOfBlueObjective=ShadesOfBlueObjective.instance();
        Window window = new Window("BasicMap",0,matrix,WindowBuilder.getLevelPaths().get(0));
        player = new Player("name",1, window,shadesOfBlueObjective);
    }

    @Test
    public void testEvalPoints(){
        assertEquals(0,shadesOfBlueObjective.evalPoints(player));
        mapDatabase.sixSameColoredDice(Color.BLUE);
        matrix = mapDatabase.getMatrix();
        Window window = new Window("sixSameColoredDiceMap",0,matrix, WindowBuilder.getLevelPaths().get(0));
        player = new Player("name",1, window,shadesOfBlueObjective);
        assertEquals(21,shadesOfBlueObjective.evalPoints(player));
    }
}
