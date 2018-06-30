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

public class TestShadesOfGreenObjective {
    private Square[][] matrix;
    private MapDatabase mapDatabase;
    private ShadesOfGreenObjective shadesOfGreenObjective;
    private Player player;

    @Before
    public void init(){
        mapDatabase = new MapDatabase();
        mapDatabase.standardWhiteMatrix();
        matrix = mapDatabase.getMatrix();
        shadesOfGreenObjective=ShadesOfGreenObjective.instance("imagePath");
        shadesOfGreenObjective=ShadesOfGreenObjective.instance("imagePath");
        Window window = new Window("BasicMap",0,matrix, WindowBuilder.getLevelPaths().get(0));
        player = new Player("name",1, window,shadesOfGreenObjective);
    }

    @Test
    public void testEvalPoints(){
        assertEquals(0,shadesOfGreenObjective.evalPoints(player));
        mapDatabase.sixSameColoredDice(Color.GREEN);
        matrix = mapDatabase.getMatrix();
        Window window = new Window("sixSameColoredDiceMap",0,matrix,WindowBuilder.getLevelPaths().get(0));
        player = new Player("name",1, window,shadesOfGreenObjective);
        assertEquals(21,shadesOfGreenObjective.evalPoints(player));
    }
}
